package pub.edholm.irc2rss.notification

import io.micrometer.core.instrument.MeterRegistry
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.event.EventListener
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import pub.edholm.irc2rss.Properties
import pub.edholm.irc2rss.domain.Category
import pub.edholm.irc2rss.domain.Release
import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

@Component
class SonarrNotifier(
  private val properties: Properties,
  private val restTemplate: RestTemplate,
  private val meterRegistry: MeterRegistry
) {

  private val logger: Logger = LoggerFactory.getLogger(SonarrNotifier::class.java)

  @Async
  @EventListener
  fun onEvent(event: AnnounceEvent) {
    properties.sonarr.forEach {
      pushRelease(it, event)
    }
  }

  private fun pushRelease(sonarr: Properties.Sonarr, event: AnnounceEvent) {
    if (!shouldNotify(sonarr, event.release.category)) return
    if (sonarr.url.isBlank()) {
      logger.warn("Hook enabled, but URL is blank")
      return
    }

    logger.debug("Pushing ${event.release.title} to ${sonarr.appName} (${sonarr.url})")

    val releasePush = event.let {
      ReleasePush(
        title = it.release.title,
        downloadUrl = it.release.link,
        publishDate = DateTimeFormatter.ISO_DATE_TIME.withZone(ZoneOffset.UTC).format(it.release.datePublished)
      )
    }

    val headers = HttpHeaders()
    headers.set("X-Api-Key", sonarr.apiKey)
    val entity = HttpEntity(releasePush, headers)

    val response =
      restTemplate.exchange("${sonarr.url}/api/release/push", HttpMethod.POST, entity, SonarrResponse::class.java)

    if (response.statusCode != HttpStatus.OK) {
      logger.warn("Release push failed. Expected 200 OK, got: ${response.statusCode}")
      return
    }

    val body = response.body ?: return
    meterRegistry.counter(
      "irc2rss.release.push",
      "appName",
      sonarr.appName,
      "code",
      response.statusCode.toString(),
      "downloadAllowed",
      body.downloadAllowed.toString(),
      "rejected",
      body.rejected.toString()
    ).increment()
    logger.trace("Release push successful: {}", response.body)
    if (body.approved) {
      logger.info("${event.release.title} was approved by ${sonarr.appName}")
    } else {
      logger.info("${event.release.title} was rejected by ${sonarr.appName} due to ${body.rejections}")
    }
  }

  private fun shouldNotify(sonarr: Properties.Sonarr, category: Category): Boolean =
    sonarr.categories.contains(category)
}

data class ReleasePush(
  val title: String,
  val downloadUrl: String,
  val protocol: String = "torrent",
  val publishDate: String
)

data class SonarrResponse(
  val guid: String,
  val qualityWeight: Long,
  val age: Long,
  val ageHours: Double,
  val ageMinutes: Double,
  val approved: Boolean,
  val temporarilyRejected: Boolean,
  val rejected: Boolean,
  val rejections: List<String>,
  val publishDate: Instant,
  val downloadUrl: String,
  val downloadAllowed: Boolean,
  val releaseWeight: Long,
  val protocol: String
)

data class AnnounceEvent(val release: Release)
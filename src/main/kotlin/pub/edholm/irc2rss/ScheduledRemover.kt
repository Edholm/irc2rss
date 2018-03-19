package pub.edholm.irc2rss

import io.micrometer.core.instrument.MeterRegistry
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import pub.edholm.irc2rss.database.ReleaseRepository
import java.time.Duration
import java.time.Instant

@Component
class ScheduledRemover(
  private val releaseRepository: ReleaseRepository,
  private val meterRegistry: MeterRegistry
) {
  companion object {
    const val MAXIMUM_AGE_IN_SECONDS = 72 * 60 * 60L
  }

  val log: Logger = LoggerFactory.getLogger(ScheduledRemover::class.java)

  @Scheduled(initialDelay = 10000L, fixedDelay = 172800000)
  fun removeOldReleases() {
    val releasesTooOld = releaseRepository
      .findAll()
      .filter {
        Duration.between(it.datePublished, Instant.now())
          .abs()
          .seconds > MAXIMUM_AGE_IN_SECONDS
      }

    log.info("Removing ${releasesTooOld.size} releases that are older than 72h")
    log.debug("Removing the following releases: ${releasesTooOld.map { it.title }}")

    meterRegistry.counter("irc2rss.releases.removed").increment(releasesTooOld.count().toDouble())
    releaseRepository.deleteAll(releasesTooOld)
  }
}
package pub.edholm.irc2rss.irc

import io.micrometer.core.instrument.MeterRegistry
import org.pircbotx.Colors
import org.pircbotx.hooks.ListenerAdapter
import org.pircbotx.hooks.events.MessageEvent
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Component
import pub.edholm.irc2rss.AnnounceEvent
import pub.edholm.irc2rss.HookManager
import pub.edholm.irc2rss.Properties
import pub.edholm.irc2rss.database.ReleaseRepository
import pub.edholm.irc2rss.domain.Category
import pub.edholm.irc2rss.domain.CategoryConverter
import pub.edholm.irc2rss.domain.Release
import java.util.*

@Component
class AnnounceListener(
  private val releaseRepository: ReleaseRepository,
  private val properties: Properties,
  private val meterRegistry: MeterRegistry,
  private val eventPublisher: ApplicationEventPublisher
) : ListenerAdapter() {

  private val logger: Logger = LoggerFactory.getLogger(AnnounceListener::class.java)

  companion object {
    val announceRegex =
      Regex("^New Torrent Announcement:\\s*<([^>]*)>\\s*Name:'(.*)' uploaded by '([^']*)'\\s*-\\s*https?://([^/]+/)torrent/(\\d+)")
  }

  override fun onMessage(event: MessageEvent) {
    val msgWithoutColors = Colors.removeFormattingAndColors(event.message)
    logger.debug("Received message: $msgWithoutColors")
    val parts = splitAnnouncement(msgWithoutColors) ?: return
    logger.debug("Title: ${parts.title} - ${parts.parsedCategory}")

    val link = constructDownloadLink(parts)
    val release = Release(
      title = parts.title,
      category = parts.parsedCategory,
      originalCategory = parts.category,
      torrentId = parts.torrentId,
      link = link
    )

    logger.debug("Parsed release: $release")
    meterRegistry.counter("irc2rss.releases.added", "category", release.category.name).increment()

    val savedRelease = releaseRepository.save(release)
    eventPublisher.publishEvent(AnnounceEvent(savedRelease))
  }

  private fun splitAnnouncement(announcement: String): Announcement? {
    val groups = announceRegex.matchEntire(announcement)?.groupValues ?: return null

    val category = CategoryConverter.fromTorrentLeech(groups[1])
    val title = groups[2].replace(" ", ".")
    val uploadedBy = groups[3]
    val torrentId = groups[5].toLong()

    return Announcement(title, category, groups[1], uploadedBy, torrentId)
  }

  private fun constructDownloadLink(announcement: Announcement): String {
    return "https://www.torrentleech.org/rss/download/${announcement.torrentId}/${properties.torrentleech.rsskey}/${announcement.title}.torrent"
  }

  data class Announcement(
    val title: String,
    val parsedCategory: Category,
    val category: String,
    val uploadedBy: String,
    val torrentId: Long
  )
}
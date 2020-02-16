package pub.edholm.irc2rss.irc

import org.pircbotx.Colors
import org.pircbotx.hooks.ListenerAdapter
import org.pircbotx.hooks.events.MessageEvent
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Component
import pub.edholm.irc2rss.Properties
import pub.edholm.irc2rss.domain.Category
import pub.edholm.irc2rss.domain.CategoryConverter
import pub.edholm.irc2rss.domain.Release
import pub.edholm.irc2rss.notification.AnnounceEvent

@Component
class AnnounceListener(
  private val properties: Properties,
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

    val link = constructDownloadLink(parts)
    val release = Release(
      id = parts.torrentId,
      title = parts.title,
      category = parts.parsedCategory,
      originalCategory = parts.category,
      link = link
    )

    logger.debug("Parsed release: $release")
    eventPublisher.publishEvent(AnnounceEvent(release))
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
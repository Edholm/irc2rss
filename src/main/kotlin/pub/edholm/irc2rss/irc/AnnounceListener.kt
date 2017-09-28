package pub.edholm.irc2rss.irc

import org.pircbotx.Colors
import org.pircbotx.hooks.ListenerAdapter
import org.pircbotx.hooks.events.MessageEvent
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import pub.edholm.irc2rss.Properties
import pub.edholm.irc2rss.database.ReleaseRepository
import pub.edholm.irc2rss.domain.Category
import pub.edholm.irc2rss.domain.CategoryCoverter
import pub.edholm.irc2rss.domain.Release

@Component
class AnnounceListener(private val releaseRepository: ReleaseRepository,
                       private val properties: Properties,
                       private val logger: Logger = LoggerFactory.getLogger(AnnounceListener::class.java)) : ListenerAdapter() {
  companion object {
    val announceRegex = Regex("^New Torrent Announcement:\\s*<([^>]*)>\\s*Name:'(.*)' uploaded by '([^']*)'\\s*-\\s*https?\\:\\/\\/([^\\/]+\\/)torrent/(\\d+)")
  }

  override fun onMessage(event: MessageEvent) {
    logger.debug("Received message: " + event.message)
    val parts = splitAnnouncement(event.message) ?: return
    logger.debug("Split parts: " + parts)

    val link = constructDownloadLink(parts)
    val release = Release(title = parts.title, category = parts.category, torrentId = parts.torrentId, link = link)

    logger.debug("Parsed release: " + release)
    releaseRepository.save(release)
  }

  private fun splitAnnouncement(announcement: String): AnnouncementDTO? {
    val withoutColor = Colors.removeFormattingAndColors(announcement)
    val groups = announceRegex.matchEntire(withoutColor)?.groupValues ?: return null

    val category = CategoryCoverter.fromTorrentLeech(groups[1])
    val title = groups[2].replace(" ", ".")
    val uploadedBy = groups[3]
    val torrentId = groups[5].toLong()

    return AnnouncementDTO(title, category, uploadedBy, torrentId)
  }

  private fun constructDownloadLink(announcement: AnnouncementDTO): String {
    return "https://www.torrentleech.org/rss/download/${announcement.torrentId}/${properties.torrentleech.rsskey}/${announcement.title}.torrent"
  }

  data class AnnouncementDTO(val title: String,
                             val category: Category,
                             val uploadedBy: String,
                             val torrentId: Long)
}
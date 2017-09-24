package pub.edholm.irc2rss.irc

import org.pircbotx.hooks.ListenerAdapter
import org.pircbotx.hooks.events.MessageEvent
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import pub.edholm.irc2rss.database.ReleaseRepository
import pub.edholm.irc2rss.domain.Category
import pub.edholm.irc2rss.domain.CategoryCoverter
import pub.edholm.irc2rss.domain.Release

@Component
class AnnounceListener(private val releaseRepository: ReleaseRepository,
                       @Value("\${irc2rss.torrentleech.rsskey}") private val rssKey: String) : ListenerAdapter() {
  companion object {
    val announceRegex = Regex("^New Torrent Announcement:\\s*<([^>]*)>\\s*Name:'(.*)' uploaded by '([^']*)'\\s*-\\s*https?\\:\\/\\/([^\\/]+\\/)torrent/(\\d+)")
  }

  override fun onMessage(event: MessageEvent) {
    val parts = splitAnnouncement(event.message) ?: return

    val link = constructDownloadLink(parts)
    val release = Release(title = parts.title, category = parts.category, torrentId = parts.torrentId, link = link)
    releaseRepository.save(release)
  }

  private fun splitAnnouncement(announcement: String): AnnouncementDTO? {
    val groups = announceRegex.matchEntire(announcement)?.groupValues ?: return null

    val category = CategoryCoverter.fromTorrentLeech(groups[1])
    val title = groups[2].replace(" ", ".")
    val uploadedBy = groups[3]
    val torrentId = groups[5].toLong()

    return AnnouncementDTO(title, category, uploadedBy, torrentId)
  }

  private fun constructDownloadLink(announcement: AnnouncementDTO): String {
    return "https://www.torrentleech.org/rss/download/${announcement.torrentId}/$rssKey/${announcement.title}.torrent"
  }

  data class AnnouncementDTO(val title: String,
                             val category: Category,
                             val uploadedBy: String,
                             val torrentId: Long)
}
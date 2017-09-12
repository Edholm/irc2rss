package pub.edholm.irc2rss.rss

import com.rometools.rome.feed.synd.*
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Component
import pub.edholm.irc2rss.database.ReleaseRepository
import pub.edholm.irc2rss.domain.Release
import java.util.*

@Component
class FeedFactory(private val releaseRepository: ReleaseRepository) {
  companion object {
    const val FEED_SIZE = 15
  }

  fun tlFeed(): SyndFeed {

    val feed = SyndFeedImpl()
    feed.feedType = "rss_2.0"
    feed.title = "TorrentLeech IRC to RSS"
    feed.description = "Auto converted RSS from IRC"
    feed.link = "https://www.torrentleech.org/"

    feed.entries = releaseRepository
      .findAll(PageRequest.of(0, FEED_SIZE, Sort.by(Sort.Direction.DESC, "datePublished")))
      .toList()
      .map { it.toSyndEntry() }

    return feed
  }

  private fun Release.toSyndEntry(): SyndEntry {
    val entry = SyndEntryImpl()
    entry.title = this.title
    entry.publishedDate = Date.from(this.datePublished)
    entry.link = this.link
    entry.comments = "https://www.torrentleech.org/torrent/${this.torrentId}#comments$"
    entry.uri = entry.link

    val desc = SyndContentImpl()
    desc.value = "Category: ${this.category}"
    entry.description = desc

    return entry
  }
}
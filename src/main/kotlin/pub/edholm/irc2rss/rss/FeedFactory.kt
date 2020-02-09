package pub.edholm.irc2rss.rss

import com.rometools.rome.feed.synd.SyndContentImpl
import com.rometools.rome.feed.synd.SyndEntry
import com.rometools.rome.feed.synd.SyndEntryImpl
import com.rometools.rome.feed.synd.SyndFeed
import com.rometools.rome.feed.synd.SyndFeedImpl
import org.springframework.stereotype.Component
import pub.edholm.irc2rss.Properties
import pub.edholm.irc2rss.domain.Category
import pub.edholm.irc2rss.domain.Release
import pub.edholm.irc2rss.services.ReleaseService
import java.util.*

@Component
class FeedFactory(
  private val releaseService: ReleaseService,
  private val properties: Properties
) {
  companion object {
    const val FEED_SIZE = 15
  }

  fun tlFeed(categories: Collection<Category> = getCategoriesToShow(), size: Int = FEED_SIZE): SyndFeed {

    val feed = SyndFeedImpl()
    feed.feedType = "rss_2.0"
    feed.title = "TorrentLeech IRC to RSS"
    feed.description = "Auto converted RSS from IRC"
    feed.link = "https://www.torrentleech.org/"

    feed.entries = releaseService.getCategories(categories)
      .map { it.toSyndEntry() }

    return feed
  }

  fun getCategoriesToShow(): Collection<Category> {
    return if (properties.category.filter.isEmpty()) EnumSet.allOf(Category::class.java)
    else properties.category.filter
  }

  private fun Release.toSyndEntry(): SyndEntry {
    val entry = SyndEntryImpl()
    entry.title = this.title
    entry.publishedDate = Date.from(this.datePublished)
    entry.link = this.link
    entry.comments = "https://www.torrentleech.org/torrent/${this.id}#comments"
    entry.uri = entry.link

    val desc = SyndContentImpl()
    desc.value = "Category: ${this.category}"
    entry.description = desc

    return entry
  }
}
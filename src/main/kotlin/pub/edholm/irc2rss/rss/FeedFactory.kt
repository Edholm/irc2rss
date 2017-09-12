package pub.edholm.irc2rss.rss

import com.rometools.rome.feed.synd.SyndContentImpl
import com.rometools.rome.feed.synd.SyndEntryImpl
import com.rometools.rome.feed.synd.SyndFeed
import com.rometools.rome.feed.synd.SyndFeedImpl
import org.springframework.stereotype.Component

@Component
class FeedFactory {
    fun tlFeed(): SyndFeed {

        val feed = SyndFeedImpl()
        feed.feedType = "rss_2.0"
        feed.title = "TorrentLeech IRC to RSS"
        feed.description = "Auto converted RSS from IRC"
        feed.link = "https://www.torrentleech.org/"

        val entry = SyndEntryImpl()
        entry.title = "Herp derp"

        val entry2 = SyndEntryImpl()
        entry2.title = "Gherp derp"

        val content = SyndContentImpl()
        content.value = "testy test content"

        entry.contents = listOf(content)

        feed.entries = listOf(entry, entry2)

        return feed
    }
}
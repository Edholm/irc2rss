package pub.edholm.irc2rss.controllers

import com.rometools.rome.io.SyndFeedOutput
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import pub.edholm.irc2rss.domain.Category
import pub.edholm.irc2rss.rss.FeedFactory

@RestController
@RequestMapping("/rss")
class RssController(private val feedFactory: FeedFactory) {
  @GetMapping(value = ["/torrentleech"], produces = [(MediaType.APPLICATION_RSS_XML_VALUE)])
  fun torrentleech(@RequestParam categories: String?): String {
    return if (categories == null) {
      SyndFeedOutput().outputString(feedFactory.tlFeed())
    } else {
      SyndFeedOutput().outputString(feedFactory.tlFeed(convertCommaCategories(categories)))
    }
  }

  private fun convertCommaCategories(commaSeparatedCategories: String): Collection<Category> {
    return commaSeparatedCategories
      .split(",")
      .map { Category.valueOf(it) }
  }
}
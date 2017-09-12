package pub.edholm.irc2rss.rss

import com.rometools.rome.io.SyndFeedOutput
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/rss")
class RssController(private val feedFactory: FeedFactory) {
    @GetMapping(value = "/torrentleech", produces = arrayOf(MediaType.APPLICATION_RSS_XML_VALUE))
    fun torrentleech(): Mono<String> = Mono.just(SyndFeedOutput().outputString(feedFactory.tlFeed()))
}
package pub.edholm.irc2rss.controllers

import org.springframework.data.domain.Sort
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import pub.edholm.irc2rss.database.ReleaseRepository
import pub.edholm.irc2rss.domain.Category
import pub.edholm.irc2rss.domain.Release
import java.time.Duration
import java.time.Instant
import java.time.ZoneId

@RestController
@RequestMapping("/releases")
class ReleaseController(private val releaseRepository: ReleaseRepository) {

  @GetMapping
  fun all(): List<Release> = releaseRepository.findAll(Sort.by(Sort.Direction.DESC, "datePublished", "title"))

  @GetMapping("/{category}")
  fun getSpecificCategory(@PathVariable category: Category): List<Release> = releaseRepository.findByCategory(category)

  @GetMapping("/last")
  fun lastRelease(): Map<String, Any?> {
    val lastRelease = releaseRepository.findFirstByOrderByDatePublishedDesc()
    val datePublished = lastRelease[0].datePublished
    return mapOf(
      "lastRelease" to lastRelease[0].title,
      "lastPublishedDate" to datePublished,
      "lastPublishedDateLocal" to datePublished.atZone(ZoneId.of("Europe/Stockholm")),
      "seconds-to-now" to Duration.between(datePublished, Instant.now()).seconds
    )
  }
}
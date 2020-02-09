package pub.edholm.irc2rss.controllers

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import pub.edholm.irc2rss.domain.Category
import pub.edholm.irc2rss.domain.Release
import pub.edholm.irc2rss.services.ReleaseService

@RestController
@RequestMapping("/releases")
class ReleaseController(private val releaseService: ReleaseService) {

  @GetMapping
  fun all(): List<Release> = releaseService.getAll()

  @GetMapping("/{category}")
  fun getSpecificCategory(@PathVariable category: Category): List<Release> = releaseService.getCategory(category)

  /*
  @GetMapping("/last")
  fun lastRelease(): Map<String, Any?> {
    val lastRelease = listOf<Release>() //releaseRepository.findFirstByOrderByDatePublishedDesc()
    val datePublished = lastRelease[0].datePublished
    return mapOf(
      "lastRelease" to lastRelease[0].title,
      "lastPublishedDate" to datePublished,
      "lastPublishedDateLocal" to datePublished.atZone(ZoneId.of("Europe/Stockholm")),
      "seconds-to-now" to Duration.between(datePublished, Instant.now()).seconds
    )
  }
   */
}
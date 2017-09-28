package pub.edholm.irc2rss.filtering

import org.springframework.web.bind.annotation.*
import pub.edholm.irc2rss.Properties
import pub.edholm.irc2rss.domain.Category

@RestController
@RequestMapping("/filter")
class FilterController(private val properties: Properties) {
  @GetMapping
  fun listCategoryFilters(): Set<Category> = properties.torrentleech.categoryFilter

  @PutMapping
  fun addNewCategoryFilter(@RequestBody categories: List<Category>): Set<Category> {
    properties.torrentleech.categoryFilter.addAll(categories)
    return properties.torrentleech.categoryFilter.toSet()
  }

  @DeleteMapping
  fun deleteCategory(@RequestBody categoriesToDelete: List<Category>): Set<Category> {
    properties.torrentleech.categoryFilter.removeAll(categoriesToDelete)
    return properties.torrentleech.categoryFilter.toSet()
  }
}
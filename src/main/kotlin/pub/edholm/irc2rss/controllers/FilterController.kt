package pub.edholm.irc2rss.controllers

import org.springframework.web.bind.annotation.*
import pub.edholm.irc2rss.Properties
import pub.edholm.irc2rss.domain.Category

@RestController
@RequestMapping("/filter")
class FilterController(private val properties: Properties) {
  @GetMapping
  fun listCategoryFilters(): Set<Category> = properties.category.filter

  @PutMapping
  fun addNewCategoryFilter(@RequestBody categories: List<Category>): Set<Category> {
    properties.category.filter.addAll(categories)
    return listCategoryFilters()
  }

  @DeleteMapping
  fun deleteCategory(@RequestBody categoriesToDelete: List<Category>): Set<Category> {
    properties.category.filter.removeAll(categoriesToDelete)
    return listCategoryFilters()
  }
}
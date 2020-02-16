package pub.edholm.irc2rss.services

import io.micrometer.core.instrument.MeterRegistry
import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import pub.edholm.irc2rss.Properties
import pub.edholm.irc2rss.domain.Category
import pub.edholm.irc2rss.domain.Release
import pub.edholm.irc2rss.domain.ReleasesList
import pub.edholm.irc2rss.notification.AnnounceEvent

@Service
class ReleaseService(private val meterRegistry: MeterRegistry, private val properties: Properties) {
  private val releases: MutableMap<Category, ReleasesList> = mutableMapOf()

  fun getAll(): List<Release> {
    return releases.values.flatten()
      .sortedByDatePublished()
  }

  fun getCategory(c: Category): List<Release> {
    return releases.getOrDefault(c, ReleasesList(1))
      .toList()
      .sortedByDatePublished()
  }

  fun getCategories(cats: Collection<Category>): List<Release> {
    return cats.map { releases.getOrDefault(it, ReleasesList()) }
      .flatten()
      .sortedByDatePublished()
  }

  @Async
  @EventListener
  fun onEvent(event: AnnounceEvent) {
    add(event.release)
  }

  fun add(release: Release) {
    meterRegistry.counter("irc2rss.releases.added", "category", release.category.name).increment()
    val releasesList = releases.getOrDefault(release.category, ReleasesList(properties.category.maxSize))
    releasesList.add(release)
    releases.putIfAbsent(release.category, releasesList)
  }

  private fun Iterable<Release>.sortedByDatePublished(): List<Release> {
    return this.sortedWith(compareBy(Release::datePublished, Release::title).reversed())
  }
}
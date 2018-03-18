package pub.edholm.irc2rss

import io.micrometer.core.instrument.MeterRegistry
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import pub.edholm.irc2rss.domain.Category
import pub.edholm.irc2rss.domain.Release

@Component
class HookManager(
  private val properties: Properties,
  private val restTemplate: RestTemplate,
  private val meterRegistry: MeterRegistry
) {

  private val logger: Logger = LoggerFactory.getLogger(HookManager::class.java)

  @Async
  fun executeHook(release: Release) {
    val hook = properties.hook
    if (!hookShouldExecute(release.category)) return
    if (hook.url.isBlank()) {
      logger.warn("Hook enabled, but URL is blank")
      return
    }

    logger.debug("Executing hook on ${hook.url} …")
    val response = restTemplate.getForEntity(hook.url, String::class.java)
    meterRegistry.counter("irc2rss.hook.executed", "code", response.statusCode.toString()).increment()

    if (response.statusCodeValue != hook.expectedReturnCode) {
      logger.warn("Hook failed. Expected ${hook.expectedReturnCode}, got: ${response.statusCode}")
      return
    }
    logger.debug("Hook successful")
  }

  private fun hookShouldExecute(category: Category): Boolean {
    val filters = properties.category.filter

    if (!properties.hook.enabled) return false
    if (!properties.hook.onlyHookOnFiltered) return true

    if (filters.isEmpty()) return true

    return filters.contains(category)
  }
}
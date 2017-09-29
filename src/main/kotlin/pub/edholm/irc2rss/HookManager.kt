package pub.edholm.irc2rss

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate

@Component
class HookManager(private val properties: Properties,
                  private val restTemplate: RestTemplate,
                  private val logger: Logger = LoggerFactory.getLogger(HookManager::class.java)) {

  @Async
  fun executeHook() {
    val hook = properties.hook
    if (!hook.enabled) return
    if (hook.url.isBlank()) {
      logger.warn("Hook enabled, but URL is blank")
      return
    }

    logger.debug("Executing hook on ${hook.url} …")
    val response = restTemplate.getForEntity(hook.url, String::class.java)
    if (response.statusCodeValue != hook.expectedReturnCode) {
      logger.warn("Hook failed. Expected ${hook.expectedReturnCode}, got: ${response.statusCode}")
      return
    }
    logger.debug("Hook successful: ${response.body}")
  }
}
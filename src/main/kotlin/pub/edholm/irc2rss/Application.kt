package pub.edholm.irc2rss

import io.micrometer.core.instrument.MeterRegistry
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.web.client.RestTemplate
import java.time.Duration

@SpringBootApplication
@ConfigurationPropertiesScan("pub.edholm.irc2rss")
@EnableScheduling
@EnableAsync
class Application {

  @Bean
  fun getRestTemplate(): RestTemplate = RestTemplateBuilder()
    .setConnectTimeout(Duration.ofSeconds(10))
    .build()

  @Bean
  fun startBot(tlBot: TLBot, properties: Properties) =
    CommandLineRunner {
      if (properties.torrentleech.enabled) {
        tlBot.start()
      }
    }

  @Bean
  fun commonTags(): MeterRegistryCustomizer<MeterRegistry> {
    val appEnv = System.getenv("APP_ENV") ?: "devel"
    return MeterRegistryCustomizer {
      it.config().commonTags("app", "irc2rss", "env", appEnv)
    }
  }
}

fun main(args: Array<String>) {
  SpringApplication.run(Application::class.java, *args)
}
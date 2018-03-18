package pub.edholm.irc2rss

import io.micrometer.core.instrument.Meter
import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.core.instrument.config.MeterFilter
import org.pircbotx.Configuration
import org.pircbotx.PircBotX
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.web.client.RestTemplate
import pub.edholm.irc2rss.irc.AnnounceListener
import javax.net.SocketFactory
import javax.net.ssl.SSLSocketFactory

@SpringBootApplication
@EnableMongoRepositories
@EnableScheduling
@EnableAsync
class Application {

  @Bean
  fun getRestTemplate(): RestTemplate = RestTemplateBuilder()
    .setConnectTimeout(10000)
    .build()

  @Bean
  fun torrentleechIrcBot(announceListener: AnnounceListener, properties: Properties): PircBotX {
    val socketFactory = if (properties.torrentleech.ssl) SSLSocketFactory.getDefault() else SocketFactory.getDefault()
    val config = Configuration.Builder()
      .setName(properties.torrentleech.nick)
      .setLogin(properties.torrentleech.nick)
      .setRealName(properties.torrentleech.nick)
      .setAutoNickChange(false)
      .setAutoReconnect(true)
      .setAutoReconnectDelay(13377)
      .setAutoReconnectAttempts(100)
      .addAutoJoinChannel(properties.torrentleech.autojoinChannel)
      .addServer(properties.torrentleech.host, properties.torrentleech.port)
      .setSocketFactory(socketFactory)
      .addListener(announceListener)
      .setNickservPassword(properties.torrentleech.nickservPwd)
      .buildConfiguration()

    return PircBotX(config)
  }

  @Bean
  fun startBot(tlBot: TLBot, properties: Properties) = CommandLineRunner {
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

  @Bean
  fun appendPrefixToMetrics(): MeterFilter {
    return object : MeterFilter {
      override fun map(id: Meter.Id): Meter.Id {
        if (id.name.startsWith("torrentleech")) {
          return id.withName("irc2rss.${id.name}")
        }
        return id
      }
    }
  }
}

fun main(args: Array<String>) {
  SpringApplication.run(Application::class.java, *args)
}
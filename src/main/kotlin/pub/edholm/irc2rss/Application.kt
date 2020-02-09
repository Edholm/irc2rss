package pub.edholm.irc2rss

import io.micrometer.core.instrument.MeterRegistry
import org.pircbotx.PircBotX
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
import pub.edholm.irc2rss.irc.AnnounceListener
import java.time.Duration
import javax.net.SocketFactory
import javax.net.ssl.SSLSocketFactory

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
  fun torrentleechIrcBot(announceListener: AnnounceListener, properties: Properties): PircBotX {
    val socketFactory = if (properties.torrentleech.ssl) SSLSocketFactory.getDefault() else SocketFactory.getDefault()
    val config = org.pircbotx.Configuration.Builder()
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
  fun startBot(tlBot: TLBot, properties: Properties, meterRegistry: MeterRegistry, tlBotMeterBinder: TLBotMeterBinder) =
    CommandLineRunner {
      tlBotMeterBinder.bindTo(meterRegistry)
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
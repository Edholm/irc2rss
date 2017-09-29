package pub.edholm.irc2rss

import org.pircbotx.Configuration
import org.pircbotx.PircBotX
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
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
  fun getRestTemplate() = RestTemplate()

  @Bean
  fun torrentleechIrcBot(announceListener: AnnounceListener, properties: Properties): PircBotX {
    val socketFactory = if (properties.torrentleech.ssl) SSLSocketFactory.getDefault() else SocketFactory.getDefault()
    val config = Configuration.Builder()
      .setName(properties.torrentleech.nick)
      .setLogin(properties.torrentleech.nick)
      .setRealName(properties.torrentleech.nick)
      .setAutoNickChange(false)
      .setAutoReconnect(true)
      .setAutoReconnectDelay(5337)
      .setAutoReconnectAttempts(10)
      .addAutoJoinChannel(properties.torrentleech.autojoinChannel)
      .addServer(properties.torrentleech.host, properties.torrentleech.port)
      .setSocketFactory(socketFactory)
      .addListener(announceListener)
      .setNickservPassword(properties.torrentleech.nickservPwd)
      .buildConfiguration()

    return PircBotX(config)
  }

  @Bean
  fun startBot(tlBot: PircBotX, properties: Properties) = CommandLineRunner {
    if (properties.torrentleech.enabled) {
      tlBot.startBot()
    }
  }
}

fun main(args: Array<String>) {
  SpringApplication.run(Application::class.java, *args)
}
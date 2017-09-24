package pub.edholm.irc2rss

import org.pircbotx.Configuration
import org.pircbotx.PircBotX
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories
import org.springframework.scheduling.annotation.EnableScheduling
import pub.edholm.irc2rss.irc.AnnounceListener

@SpringBootApplication
@EnableMongoRepositories
@EnableScheduling
class Application {

  @Bean
  fun torrentleechIrcBot(announceListener: AnnounceListener): PircBotX {
    val config = Configuration.Builder()
      .setName("Edholm")
      .setLogin("Edholm")
      .setRealName("Edholm")
      .setAutoNickChange(false)
      .setAutoReconnect(true)
      .setAutoReconnectDelay(5337)
      .setAutoReconnectAttempts(10)
      .addAutoJoinChannel("#tlannounces")
      .addServer("irc.torrentleech.org", 7011)
      //.setSocketFactory(SSLSocketFactory.getDefault())
      .addListener(announceListener)
      .buildConfiguration()
    return PircBotX(config)
  }

  @Bean
  fun startBot(tlBot: PircBotX) = CommandLineRunner {
    tlBot.startBot()
    // TODO: auth with nickserv
  }
}

fun main(args: Array<String>) {
  SpringApplication.run(Application::class.java, *args)
}
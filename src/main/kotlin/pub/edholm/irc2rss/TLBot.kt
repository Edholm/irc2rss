package pub.edholm.irc2rss

import org.pircbotx.PircBotX
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component

@Component
class TLBot(private val tlBot: PircBotX) {
  @Async
  fun start() {
    tlBot.startBot()
  }
}
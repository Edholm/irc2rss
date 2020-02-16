package pub.edholm.irc2rss.controllers

import org.pircbotx.PircBotX
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import pub.edholm.irc2rss.TLBot

@RestController
@RequestMapping("/irc", produces = [MediaType.APPLICATION_JSON_VALUE])
class IrcBotController(private val tlBot: TLBot) {

  @GetMapping("/torrentleech")
  fun tlBotInfo(): Map<String, Any> {
    return mapOf(
      "nick" to tlBot.bot.nick,
      "state" to tlBot.bot.state,
      "hostname" to tlBot.bot.serverHostname,
      "port" to tlBot.bot.serverPort,
      "connected" to tlBot.bot.isConnected,
      "isNickservIdentified" to tlBot.bot.isNickservIdentified
    )
  }

  @GetMapping("/torrentleech/start")
  fun startBot(): Map<String, Any> {
    if (tlBot.bot.state == PircBotX.State.DISCONNECTED) {
      tlBot.bot.startBot()
    }
    return mapOf(
      "connected" to tlBot.bot.isConnected,
      "isNickServIdentified" to tlBot.bot.isNickservIdentified
    )
  }

  @GetMapping("/torrentleech/stopReconnect")
  fun stopReconnect(): Map<String, Any> {
    tlBot.bot.stopBotReconnect()
    return mapOf(
      "connected" to tlBot.bot.isConnected,
      "isNickServIdentified" to tlBot.bot.isNickservIdentified
    )
  }

  @GetMapping("/torrentleech/disconnect")
  fun disconnect(): Map<String, Any> {
    if (tlBot.bot.state == PircBotX.State.CONNECTED) {
      tlBot.bot.send().quitServer()
    }
    return mapOf(
      "connected" to tlBot.bot.isConnected,
      "isNickServIdentified" to tlBot.bot.isNickservIdentified
    )
  }

}
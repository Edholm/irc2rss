package pub.edholm.irc2rss.irc

import org.pircbotx.PircBotX
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/irc", produces = arrayOf(MediaType.APPLICATION_JSON_UTF8_VALUE))
class IrcBotController(private val tlBot: PircBotX) {

  @GetMapping("/torrentleech")
  fun tlBotInfo(): Map<String, Any> {
    return mapOf(
      "nick" to tlBot.nick,
      "state" to tlBot.state,
      "hostname" to tlBot.serverHostname,
      "port" to tlBot.serverPort,
      "connected" to tlBot.isConnected,
      "isNickservIdentified" to tlBot.isNickservIdentified)
  }

  @GetMapping("/torrentleech/start")
  fun startBot(): Map<String, Any> {
    if (tlBot.state == PircBotX.State.DISCONNECTED) {
      tlBot.startBot()
    }
    return mapOf(
      "connected" to tlBot.isConnected,
      "isNickServIdentified" to tlBot.isNickservIdentified)
  }

  @GetMapping("/torrentleech/stopReconnect")
  fun stopReconnect(): Map<String, Any> {
    tlBot.stopBotReconnect()
    return mapOf(
      "connected" to tlBot.isConnected,
      "isNickServIdentified" to tlBot.isNickservIdentified)
  }

  @GetMapping("/torrentleech/disconnect")
  fun disconnect(): Map<String, Any> {
    if (tlBot.state == PircBotX.State.CONNECTED) {
      tlBot.send().quitServer()
    }
    return mapOf(
      "connected" to tlBot.isConnected,
      "isNickServIdentified" to tlBot.isNickservIdentified)
  }

}
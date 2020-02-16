package pub.edholm.irc2rss

import org.pircbotx.PircBotX
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import pub.edholm.irc2rss.irc.AnnounceListener
import javax.net.SocketFactory
import javax.net.ssl.SSLSocketFactory

@Component
class TLBot(announceListener: AnnounceListener, private val properties: Properties) {
  val bot: PircBotX

  init {
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

    bot = PircBotX(config)
  }

  fun isConnected(): Boolean = bot.isConnected

  fun isInChannel(channel: String): Boolean = bot.userChannelDao.containsChannel(channel)

  fun isInDefaultChannel(): Boolean = isInChannel(properties.torrentleech.autojoinChannel)

  fun isIdentified(): Boolean = bot.isNickservIdentified

  fun channels(): List<String> = bot.userChannelDao.allChannels.map { it.name }

  @Async
  fun start() {
    bot.startBot()
  }
}

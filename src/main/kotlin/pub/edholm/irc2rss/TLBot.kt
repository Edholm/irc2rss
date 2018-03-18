package pub.edholm.irc2rss

import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.core.instrument.Tag
import io.micrometer.core.instrument.binder.MeterBinder
import org.pircbotx.PircBotX
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component

@Component
class TLBot(private val tlBot: PircBotX, private val properties: Properties) {

  fun isConnected(): Boolean = tlBot.isConnected

  fun isInChannel(channel: String): Boolean = tlBot.userChannelDao.containsChannel(channel)

  fun isInDefaultChannel(): Boolean = isInChannel(properties.torrentleech.autojoinChannel)

  fun isIdentified(): Boolean = tlBot.isNickservIdentified

  fun channels(): List<String> = tlBot.userChannelDao.allChannels.map { it.name }

  @Async
  fun start() {
    tlBot.startBot()
  }
}

private fun Boolean.toDouble(): Double = if (this) 1.0 else 0.0

@Component
class TLBotMeterBinder(private val tlBot: TLBot, private val properties: Properties) : MeterBinder {
  override fun bindTo(registry: MeterRegistry) {
    registry.gauge("torrentleech.connected", tlBot, { it.isConnected().toDouble() })
    registry.gauge("torrentleech.identified", tlBot, { it.isIdentified().toDouble() })
    registry.gauge(
      "torrentleech.joined_default_channel",
      listOf(Tag.of("channel", properties.torrentleech.autojoinChannel)),
      tlBot,
      { it.isInDefaultChannel().toDouble() }
    )
  }
}
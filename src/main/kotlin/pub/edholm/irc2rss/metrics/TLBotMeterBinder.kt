package pub.edholm.irc2rss.metrics

import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.core.instrument.Tag
import io.micrometer.core.instrument.binder.MeterBinder
import pub.edholm.irc2rss.Properties
import pub.edholm.irc2rss.TLBot

//@Component
// FIXME: circular deps
class TLBotMeterBinder(private val tlBot: TLBot, private val properties: Properties) :
  MeterBinder {
  override fun bindTo(registry: MeterRegistry) {
    registry.gauge("irc2rss.torrentleech.connected", tlBot, { it.isConnected().toDouble() })
    registry.gauge("irc2rss.torrentleech.identified", tlBot, { it.isIdentified().toDouble() })
    registry.gauge(
      "irc2rss.torrentleech.joined_default_channel",
      listOf(
        Tag.of(
          "channel",
          properties.torrentleech.autojoinChannel
        )
      ),
      tlBot,
      { it.isInDefaultChannel().toDouble() }
    )
  }
}

private fun Boolean.toDouble(): Double = if (this) 1.0 else 0.0

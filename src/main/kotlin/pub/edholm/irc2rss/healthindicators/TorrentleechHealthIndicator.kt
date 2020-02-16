package pub.edholm.irc2rss.healthindicators

import org.springframework.boot.actuate.health.Health
import org.springframework.boot.actuate.health.HealthIndicator
import org.springframework.stereotype.Component
import pub.edholm.irc2rss.Properties
import pub.edholm.irc2rss.TLBot

@Component
class TorrentleechHealthIndicator(private val tlBot: TLBot, private val properties: Properties) : HealthIndicator {

  override fun health(): Health {
    if (!tlBot.isConnected()) {
      return Health.down().withDetail("connected", false).build()
    }

    if (!tlBot.isInChannel(properties.torrentleech.autojoinChannel)) {
      return Health.outOfService().withDetail("joined ${properties.torrentleech.autojoinChannel}", false).build()
    }

    return Health.up()
      .withDetail("identified", tlBot.isIdentified())
      .withDetail("channels", tlBot.channels())
      .build()
  }
}
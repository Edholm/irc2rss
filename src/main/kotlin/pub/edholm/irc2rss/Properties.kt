package pub.edholm.irc2rss

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties("irc2rss")
data class Properties(
  val torrentleech: Torrentleech,
  val category: Category,
  val sonarr: List<Sonarr>
) {
  data class Torrentleech(
    val enabled: Boolean,
    val host: String,
    val port: Int,
    val rsskey: String?,
    val nick: String,
    val nickservPwd: String?,
    val autojoinChannel: String,
    val ssl: Boolean
  )

  data class Category(
    val maxSize: Int,
    val filter: MutableSet<pub.edholm.irc2rss.domain.Category>
  )

  data class Sonarr(
    val appName: String,
    val enabled: Boolean,
    val url: String,
    val apiKey: String,
    val categories: List<pub.edholm.irc2rss.domain.Category>
  )
}
package pub.edholm.irc2rss

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties("irc2rss")
data class Properties(
  val torrentleech: Torrentleech,
  val category: Category,
  val hook: Hook
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

  data class Hook(
    var enabled: Boolean,
    var url: String,
    var expectedReturnCode: Int,
    var onlyHookOnFiltered: Boolean
  )
}
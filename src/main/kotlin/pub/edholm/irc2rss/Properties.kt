package pub.edholm.irc2rss

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@ConfigurationProperties("irc2rss")
@Component
class Properties {
  val torrentleech = Torrentleech()
  val category = Category()
  val hook = Hook()

  class Torrentleech {
    var enabled: Boolean = true
    var host: String = ""
    var port: Int = 0
    var rsskey: String? = null
    var nick: String = ""
    var nickservPwd: String? = null
    var autojoinChannel: String = ""
    var ssl: Boolean = false
  }

  class Category {
    var filter: MutableSet<pub.edholm.irc2rss.domain.Category> = mutableSetOf()
  }

  class Hook {
    var enabled: Boolean = true
    var url: String = ""
    var expectedReturnCode: Int = 200
    var onlyHookOnFiltered: Boolean = true
  }
}
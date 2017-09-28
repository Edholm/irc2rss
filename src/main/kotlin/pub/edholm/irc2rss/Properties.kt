package pub.edholm.irc2rss

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@ConfigurationProperties("irc2rss")
@Component
class Properties {
  var torrentleech = Torrentleech()

  class Torrentleech {
    var host: String = ""
    var port: Int = 0
    var rsskey: String? = null
    var nick: String = ""
    var nickservPwd: String? = null
    var autojoinChannel: String = ""
    var ssl: Boolean = false
  }
}
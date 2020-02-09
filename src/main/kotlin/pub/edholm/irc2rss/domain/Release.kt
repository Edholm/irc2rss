package pub.edholm.irc2rss.domain

import java.time.Instant

data class Release(
  val id: Long = 0,
  val title: String = "<insert-title-here>",
  val category: Category = Category.UNKNOWN,
  val originalCategory: String = "<missing>",
  val link: String = "https://",
  val datePublished: Instant = Instant.now()
)

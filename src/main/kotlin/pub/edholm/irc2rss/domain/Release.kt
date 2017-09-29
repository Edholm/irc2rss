package pub.edholm.irc2rss.domain

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant
import java.util.*

@Document
data class Release(
  @Id val id: UUID = UUID.randomUUID(),
  val title: String = "<insert-title-here>",
  val category: Category = Category.UNKNOWN,
  val originalCategory: String = "<missing>",
  val torrentId: Long = 0,
  val link: String = "http://",
  val datePublished: Instant = Instant.now()) {
}
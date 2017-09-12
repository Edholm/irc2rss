package pub.edholm.irc2rss.database

import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import pub.edholm.irc2rss.domain.Release
import java.util.*

@Repository
interface ReleaseRepository : MongoRepository<Release, UUID> {
}
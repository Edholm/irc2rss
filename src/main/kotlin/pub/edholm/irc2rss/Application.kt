package pub.edholm.irc2rss

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.data.mongodb.config.EnableMongoAuditing
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories

@SpringBootApplication
@EnableMongoRepositories
@EnableMongoAuditing
class Application {
}

fun main(args: Array<String>) {
  SpringApplication.run(Application::class.java, *args)
}
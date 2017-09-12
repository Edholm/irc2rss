package pub.edholm.irc2rss

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableMongoRepositories
@EnableScheduling
class Application {
}

fun main(args: Array<String>) {
  SpringApplication.run(Application::class.java, *args)
}
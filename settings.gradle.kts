rootProject.name = "irc2rss"

pluginManagement {
  val kotlinVersion: String by settings
  val springBootVersion: String by settings
  val versionsVersion: String by settings

  plugins {
    kotlin("jvm") version kotlinVersion
    id("org.jetbrains.kotlin.plugin.spring") version kotlinVersion
    id("org.springframework.boot") version springBootVersion
    id("com.github.ben-manes.versions") version versionsVersion
  }
}


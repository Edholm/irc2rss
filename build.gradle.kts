
repositories {
  mavenCentral()
  gradlePluginPortal()
}

plugins {
  kotlin("jvm")
  kotlin("kapt")
  id("org.jetbrains.kotlin.plugin.spring")
  id("org.springframework.boot")
  id("com.github.ben-manes.versions")
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
  kotlinOptions {
    jvmTarget = "12"
    freeCompilerArgs = listOf("-Xjsr305=strict")
  }
}

tasks.withType<Test> {
  useJUnitPlatform()
  testLogging {
    events("passed", "skipped", "failed")
  }
}


dependencies {
  val springBootVersion: String by project
  api(platform("org.springframework.boot:spring-boot-dependencies:$springBootVersion"))

  implementation("com.rometools:rome:1.12.2") // RSS-feeds
  implementation("org.pircbotx:pircbotx:2.1")
  api("com.fasterxml.jackson.module:jackson-module-kotlin")
  implementation("org.springframework.boot:spring-boot-starter-webflux")
  kapt("org.springframework.boot:spring-boot-configuration-processor:$springBootVersion")
  implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
  implementation("org.jetbrains.kotlin:kotlin-reflect")

  /* Metrics related */
  implementation("org.springframework.boot:spring-boot-starter-actuator")
  implementation("io.micrometer:micrometer-registry-prometheus")

  testImplementation("org.springframework.boot:spring-boot-starter-test")
  testImplementation("io.projectreactor:reactor-test")
  testImplementation("org.junit.jupiter:junit-jupiter")
}


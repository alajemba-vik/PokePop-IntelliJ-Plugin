plugins {
  id("java")
  id("org.jetbrains.kotlin.jvm") version "1.9.25"
  id("org.jetbrains.intellij.platform") version "2.2.1"
}

private val versionValue = "1.0"

group = "com.alaje.intellijplugins"
version = versionValue

repositories {
  mavenCentral()

  intellijPlatform {
    defaultRepositories()
  }
}

dependencies {
  intellijPlatform {
    intellijIdeaCommunity("2024.3.2.2")
  }
}

// Configure Gradle IntelliJ Plugin
// Read more: https://plugins.jetbrains.com/docs/intellij/tools-gradle-intellij-plugin.html
intellijPlatform {
  version = versionValue

   pluginConfiguration{
     ideaVersion{
       sinceBuild = "243"
       untilBuild = provider { null}
     }
   }

  publishing {
    token = System.getenv("PUBLISH_TOKEN")
  }

  signing {
    certificateChain = System.getenv("CERTIFICATE_CHAIN")
    privateKey = System.getenv("PRIVATE_KEY")
    password = System.getenv("PRIVATE_KEY_PASSWORD")
  }

  buildSearchableOptions = false
}

tasks {
  val javaVersion = "21"
  // Set the JVM compatibility versions
  withType<JavaCompile> {
    sourceCompatibility = javaVersion
    targetCompatibility = javaVersion
  }
  withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions.jvmTarget = javaVersion
  }
}

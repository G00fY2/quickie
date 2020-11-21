plugins {
  id("com.android.application") apply false
  kotlin("android") apply false
  id("org.jlleitschuh.gradle.ktlint")
}

subprojects {
  apply(plugin = "org.jlleitschuh.gradle.ktlint")
  ktlint {
    version.set("0.39.0")
    android.set(true)
  }
}

repositories {
  mavenCentral()
}

tasks.register<Delete>("clean") {
  delete(buildDir)
}
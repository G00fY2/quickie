object Plugins {

  object Android {
    const val application = "com.android.application"
    const val library = "com.android.library"
  }

  object Kotlin {
    const val androidGradle = "android"
    const val android = "kotlin-android"
    const val parcelize = "kotlin-parcelize"
    const val dokka = "org.jetbrains.dokka"
  }

  object Misc {
    const val detekt = "io.gitlab.arturbosch.detekt"
    const val detektFormatting = "io.gitlab.arturbosch.detekt:detekt-formatting:${Versions.detekt}"
    const val gradleVersions = "com.github.ben-manes.versions"
  }
}
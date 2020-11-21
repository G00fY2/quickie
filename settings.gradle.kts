include(":app", ":quickie")

pluginManagement {
  val kotlinVersion: String by settings
  val androidGradlePluginVersion: String by settings
  repositories {
    google()
    gradlePluginPortal()
  }
  resolutionStrategy {
    eachPlugin {
      when (requested.id.namespace) {
        "com.android" -> useModule("com.android.tools.build:gradle:$androidGradlePluginVersion")
        "org.jetbrains.kotlin" -> useVersion(kotlinVersion)
        "org.jetbrains.kotlin.plugin" -> useVersion(kotlinVersion)
      }
    }
  }
}
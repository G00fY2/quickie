include(":quickie", ":sample")

dependencyResolutionManagement {
  repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
  repositories {
    google()
    mavenCentral()
    // TODO remove repository once https://github.com/detekt/detekt/pull/3455 available
    maven(url = "https://maven.pkg.jetbrains.space/public/p/kotlinx-html/maven")
  }
}

pluginManagement {
  repositories {
    google()
    gradlePluginPortal()
  }
  resolutionStrategy {
    eachPlugin {
      if (requested.id.namespace == "com.android") useModule("com.android.tools.build:gradle:${requested.version}")
    }
  }
}
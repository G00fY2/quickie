include(":quickie", ":sample")

dependencyResolutionManagement {
  @Suppress("UnstableApiUsage")
  repositories {
    google {
      mavenContent {
        includeGroupAndSubgroups("androidx")
        includeGroupAndSubgroups("com.android")
        includeGroupAndSubgroups("com.google")
      }
    }
    mavenCentral()
  }
}

pluginManagement {
  repositories {
    google {
      @Suppress("UnstableApiUsage")
      mavenContent {
        includeGroupAndSubgroups("androidx")
        includeGroupAndSubgroups("com.android")
        includeGroupAndSubgroups("com.google")
      }
    }
    gradlePluginPortal()
    mavenCentral()
  }
}
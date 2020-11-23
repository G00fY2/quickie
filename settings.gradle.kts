include(":quickie")
include(":activity-sample")
include(":advanced-sample")
include(":fragment-sample")

project(":activity-sample").projectDir = File(rootDir, "sample/activity-sample")
project(":advanced-sample").projectDir = File(rootDir, "sample/advanced-sample")
project(":fragment-sample").projectDir = File(rootDir, "sample/fragment-sample")

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
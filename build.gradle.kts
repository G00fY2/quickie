import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask

plugins {
  id(Plugins.Misc.ktlint) version Versions.ktlintPlugin
  id(Plugins.Misc.gradleVersions) version Versions.gradleVersions
}

subprojects {
  apply(plugin = Plugins.Misc.ktlint)
  ktlint {
    version.set(Versions.ktlint)
    android.set(true)
  }
}

tasks.named("dependencyUpdates", DependencyUpdatesTask::class.java).configure {
  gradleReleaseChannel = "current"
  resolutionStrategy {
    componentSelection {
      all {
        if (isNonStable(candidate.version) && !isNonStable(currentVersion)) {
          reject("Release candidate")
        }
      }
    }
  }
}

fun isNonStable(version: String) = listOf("alpha", "beta", "rc", "cr", "m", "preview")
  .any { version.matches(".*[.\\-]$it[.\\-\\d]*".toRegex(RegexOption.IGNORE_CASE)) }

repositories {
  mavenCentral()
}

tasks.register<Delete>("clean") {
  delete(buildDir)
}
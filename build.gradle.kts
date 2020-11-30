import com.android.build.gradle.BaseExtension
import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

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
  tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions {
      allWarningsAsErrors = true
      freeCompilerArgs = listOf("-progressive")
      jvmTarget = JavaVersion.VERSION_1_8.toString()
    }
  }
  afterEvaluate {
    extensions.configure<BaseExtension> {
      compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
      }
    }
  }
}

tasks.named("dependencyUpdates", DependencyUpdatesTask::class.java).configure {
  gradleReleaseChannel = "current"
  resolutionStrategy {
    componentSelection {
      all {
        if (Utils.isNonStable(candidate.version) && !Utils.isNonStable(currentVersion)) {
          reject("Release candidate")
        }
      }
    }
  }
}

repositories {
  mavenCentral()
}

tasks.register<Delete>("clean") {
  delete(buildDir)
}
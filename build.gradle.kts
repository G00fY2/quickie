import com.android.build.gradle.BaseExtension
import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  id(Plugins.Android.application) version Versions.androidGradle apply false
  kotlin(Plugins.Kotlin.androidGradle) version Versions.kotlin apply false
  id(Plugins.Misc.detekt) version Versions.detekt apply false
  id(Plugins.Misc.gradleVersions) version Versions.gradleVersions
}

subprojects {
  apply(plugin = Plugins.Misc.detekt)
  extensions.configure<DetektExtension> {
    toolVersion = Versions.detekt
    config = files("$rootDir/config/detekt/detekt.yml")
    baseline = file("$projectDir/config/detekt/baseline.xml")
    buildUponDefaultConfig = true
    ignoredBuildTypes = listOf("release")
  }
  dependencies {
    "detektPlugins"(Plugins.Misc.detektFormatting)
  }
  tasks.withType<Detekt>().configureEach {
    jvmTarget = "1.8"
  }
  tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions {
      allWarningsAsErrors = true
      freeCompilerArgs = freeCompilerArgs + listOfNotNull(
        "-progressive",
        if (this@subprojects.name != "sample") "-Xexplicit-api=strict" else null,
      )
      jvmTarget = "1.8"
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

tasks.dependencyUpdates.configure {
  gradleReleaseChannel = "current"
  rejectVersionIf { Versions.maturityLevel(candidate.version) < Versions.maturityLevel(currentVersion) }
}

tasks.register<Delete>("clean") {
  delete(buildDir)
}
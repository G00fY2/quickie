import com.android.build.gradle.BaseExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  id(Plugins.Android.application) version Versions.androidGradle apply false
  kotlin(Plugins.Kotlin.androidGradle) version Versions.kotlin apply false
  id(Plugins.Misc.detekt) version Versions.detekt
  id(Plugins.Misc.gradleVersions) version Versions.gradleVersions
}

subprojects {
  apply(plugin = Plugins.Misc.detekt)
  detekt {
    toolVersion = Versions.detekt
    config = files("$rootDir/config/detekt/detekt.yml")
    baseline = file("$projectDir/config/detekt/baseline.xml")
    buildUponDefaultConfig = true
    ignoredBuildTypes = listOf("release")
  }
  dependencies {
    detektPlugins(Plugins.Misc.detektFormatting)
  }
  tasks.detekt {
    jvmTarget = JavaVersion.VERSION_1_8.toString()
  }
  tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions {
      allWarningsAsErrors = true
      val arguments = mutableListOf("-progressive")
      if (this@subprojects.name != "sample") arguments += "-Xexplicit-api=strict"
      freeCompilerArgs = freeCompilerArgs + arguments
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

tasks.dependencyUpdates.configure {
  gradleReleaseChannel = "current"
  rejectVersionIf { Versions.maturityLevel(candidate.version) < Versions.maturityLevel(currentVersion) }
}

tasks.register<Delete>("clean") {
  delete(buildDir)
}
import com.android.build.gradle.BaseExtension
import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
  alias(libs.plugins.android.application) apply false
  alias(libs.plugins.kotlin.androidGradle) apply false
  alias(libs.plugins.misc.detekt) apply false
  alias(libs.plugins.misc.gradleVersions)
}

subprojects {
  apply(plugin = rootProject.libs.plugins.misc.detekt.get().pluginId)
  extensions.configure<DetektExtension> {
    toolVersion = rootProject.libs.versions.detekt.get()
    config = files("$rootDir/detekt.yml")
    buildUponDefaultConfig = true
    ignoredBuildTypes = listOf("release")
  }
  dependencies {
    add("detektPlugins", rootProject.libs.misc.detektFormatting)
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
      compileSdkVersion(libs.versions.androidconfig.compileSdk.get().toInt())
      buildToolsVersion(libs.versions.androidconfig.buildTools.get())
      defaultConfig {
        minSdk = libs.versions.androidconfig.minSdk.get().toInt()
        targetSdk = libs.versions.androidconfig.targetSdk.get().toInt()
      }
      compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
      }
    }
  }
}

tasks.dependencyUpdates.configure {
  gradleReleaseChannel = "current"

  fun releaseType(version: String): Int {
    val qualifiers = listOf("alpha", "beta", "m", "rc")
    val index = qualifiers.indexOfFirst { version.matches(".*[.\\-]$it[.\\-\\d]*".toRegex(RegexOption.IGNORE_CASE)) }
    return if (index < 0) qualifiers.size else index
  }

  rejectVersionIf { releaseType(candidate.version) < releaseType(currentVersion) }
}

tasks.register<Delete>("clean") {
  delete(buildDir)
}
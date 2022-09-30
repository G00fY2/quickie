import com.android.build.gradle.BaseExtension
import com.android.build.gradle.BasePlugin
import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
  alias(libs.plugins.android.application) apply false
  alias(libs.plugins.android.library) apply false
  alias(libs.plugins.kotlin.android) apply false
  alias(libs.plugins.kotlin.parcelize) apply false
  alias(libs.plugins.kotlin.dokka) apply false
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
    jvmTarget = "11"
  }
  tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions {
      allWarningsAsErrors = true
      freeCompilerArgs = freeCompilerArgs + listOfNotNull(
        "-progressive",
        "-Xexplicit-api=strict".takeIf { (this@subprojects.name != "sample") },
      )
      jvmTarget = JavaVersion.VERSION_11.toString()
    }
  }
  plugins.withType<BasePlugin>().configureEach {
    extensions.configure<BaseExtension> {
      compileSdkVersion(libs.versions.androidconfig.compileSdk.get().toInt())
      buildToolsVersion(libs.versions.androidconfig.buildTools.get())
      defaultConfig {
        minSdk = libs.versions.androidconfig.minSdk.get().toInt()
        targetSdk = libs.versions.androidconfig.targetSdk.get().toInt()
      }
      compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
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
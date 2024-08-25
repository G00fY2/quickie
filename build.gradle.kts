import com.android.build.gradle.BaseExtension
import com.android.build.gradle.BasePlugin
import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  alias(libs.plugins.android.application) apply false
  alias(libs.plugins.android.library) apply false
  alias(libs.plugins.kotlin.android) apply false
  alias(libs.plugins.kotlin.parcelize) apply false
  alias(libs.plugins.kotlin.dokka) apply false
  alias(libs.plugins.detekt) apply false
}

subprojects {
  tasks.withType<KotlinCompile>().configureEach {
    compilerOptions {
      allWarningsAsErrors = true
      progressiveMode = true
      jvmTarget = JvmTarget.JVM_11
      if (this@subprojects.name != "sample") {
        freeCompilerArgs.add("-Xexplicit-api=strict")
      }
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

  apply(plugin = rootProject.libs.plugins.detekt.get().pluginId)
  extensions.configure<DetektExtension> {
    toolVersion = rootProject.libs.versions.detekt.get()
    config.setFrom(files("$rootDir/detekt.yml"))
    buildUponDefaultConfig = true
    ignoredBuildTypes = listOf("release")
  }
  dependencies {
    add("detektPlugins", rootProject.libs.detektFormatting)
  }
  tasks.withType<Detekt>().configureEach {
    jvmTarget = JvmTarget.JVM_11.target
  }
}
import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.CommonExtension
import com.android.build.api.dsl.LibraryExtension
import com.android.build.gradle.BasePlugin
import dev.detekt.gradle.Detekt
import dev.detekt.gradle.extensions.DetektExtension
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  alias(libs.plugins.android.application) apply false
  alias(libs.plugins.android.library) apply false
  alias(libs.plugins.kotlin.parcelize) apply false
  alias(libs.plugins.kotlin.dokka.javadoc) apply false
  alias(libs.plugins.detekt) apply false
}

buildscript {
  dependencies {
    classpath(libs.kotlin.plugin)
  }
}

subprojects {
  tasks.withType<KotlinCompile>().configureEach {
    compilerOptions {
      allWarningsAsErrors = true
      extraWarnings = true
      progressiveMode = true
      jvmTarget = JvmTarget.JVM_17
      if (this@subprojects.name != "sample") {
        freeCompilerArgs.add("-Xexplicit-api=strict")
      }
    }
  }

  plugins.withType<BasePlugin>().configureEach {
    extensions.configure<CommonExtension> {
      compileSdk {
        version = release(libs.versions.androidconfig.compileSdk.get().toInt())
        buildToolsVersion = libs.versions.androidconfig.buildTools.get()
      }
      when (this@configure) {
        is LibraryExtension -> {
          defaultConfig {
            minSdk = libs.versions.androidconfig.minSdk.get().toInt()
          }
        }
        is ApplicationExtension -> {
          defaultConfig {
            minSdk = libs.versions.androidconfig.minSdk.get().toInt()
            targetSdk = libs.versions.androidconfig.targetSdk.get().toInt()
          }
        }
      }
      compileOptions.apply {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
      }
    }
  }

  apply(plugin = rootProject.libs.plugins.detekt.get().pluginId)
  extensions.configure<DetektExtension> {
    toolVersion = rootProject.libs.versions.detekt.get()
    @Suppress("UnstableApiUsage")
    config.setFrom(isolated.rootProject.projectDirectory.file("$rootDir/detekt.yml"))
    buildUponDefaultConfig = true
    ignoredBuildTypes = listOf("release")
  }
  dependencies {
    add("detektPlugins", rootProject.libs.detekt.ktlintWrapper)
  }
  tasks.withType<Detekt>().configureEach {
    jvmTarget = JvmTarget.JVM_17.target
  }
}
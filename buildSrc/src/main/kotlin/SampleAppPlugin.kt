import com.android.build.gradle.AppExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.project
import org.gradle.kotlin.dsl.repositories

class SampleAppPlugin : Plugin<Project> {

  private val Project.android: AppExtension
    get() = extensions.findByName("android") as? AppExtension
      ?: error("Not an Android module: $name")

  override fun apply(target: Project) {
    with(target) {
      applyPlugins()
      androidConfig()
      repositoriesConfig()
      dependenciesConfig()
    }
  }

  private fun Project.applyPlugins() {
    plugins.run {
      apply(Plugins.Android.application)
      apply(Plugins.Kotlin.android)
    }
  }

  private fun Project.androidConfig() {
    android.run {
      compileSdkVersion(Config.androidCompileSdkVersion)
      buildToolsVersion(Config.buildToolsVersion)
      defaultConfig {
        minSdkVersion(Config.androidMinSdkVersion)
        targetSdkVersion(Config.androidTargetSdkVersion)
        versionCode = 1
        versionName = "1.0"
      }
      buildTypes {
        getByName("release") {
          isShrinkResources = true
          isMinifyEnabled = true
          proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
      }
      flavorDimensions("mlkit")
      productFlavors {
        create("bundled") {
          dimension("mlkit")
        }
        create("unbundled") {
          dimension("mlkit")
        }
      }
      buildFeatures.run {
        viewBinding = true
        aidl = false
        renderScript = false
        resValues = false
        shaders = false
      }
      sourceSets {
        getByName("main") {
          java.srcDirs("src/main/kotlin")
        }
      }
      compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
      }
    }
  }

  private fun Project.repositoriesConfig() {
    repositories {
      google()
      mavenCentral()
      jcenter()
    }
  }

  private fun Project.dependenciesConfig() {
    dependencies {
      "implementation"(project(":quickie"))

      "implementation"(Deps.AndroidX.appcompat)
      "implementation"(Deps.Mdc.materialDesign)
    }
  }
}
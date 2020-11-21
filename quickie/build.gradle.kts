plugins {
  id(Plugins.Android.library)
  kotlin(Plugins.Kotlin.android)
  kotlin(Plugins.Kotlin.parcelize)
}

android {
  compileSdkVersion(Config.androidCompileSdkVersion)
  defaultConfig {
    minSdkVersion(Config.androidMinSdkVersion)
  }
  buildFeatures {
    viewBinding = true
    buildConfig = false
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
  kotlinOptions {
    jvmTarget = JavaVersion.VERSION_1_8.toString()
  }
  flavorDimensions("bundleMode")
  productFlavors {
    create("bundledML") {
      dimension("bundleMode")
    }
    create("unbundledML") {
      dimension("bundleMode")
    }
  }
}

repositories {
  google()
  mavenCentral()
  jcenter {
    content {
      includeModule("org.jetbrains.trove4j", "trove4j") // required by com.android.tools.lint:lint-gradle
    }
  }
}

val bundledMLImplementation by configurations
val unbundledMLImplementation by configurations
dependencies {
  implementation(Deps.AndroidX.activity)
  implementation(Deps.AndroidX.fragment)
  implementation(Deps.AndroidX.appcompat)
  implementation(Deps.AndroidX.core)

  implementation(Deps.AndroidX.camera)
  implementation(Deps.AndroidX.cameraLifecycle)
  implementation(Deps.AndroidX.cameraPreview)

  bundledMLImplementation(Deps.MLKit.barcodeScanning)
  unbundledMLImplementation(Deps.MLKit.barcodeScanningGms)
}
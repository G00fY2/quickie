plugins {
  id("com.android.library")
  kotlin("android")
}

android {
  compileSdkVersion(30)
  defaultConfig {
    minSdkVersion(21)
  }
  buildFeatures {
    viewBinding = true
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
  implementation("androidx.activity:activity:1.2.0-beta01")
  implementation("androidx.fragment:fragment:1.3.0-beta01")
  implementation("androidx.appcompat:appcompat:1.2.0")
  implementation("androidx.core:core:1.3.2")

  implementation("androidx.camera:camera-camera2:1.0.0-beta12")
  implementation("androidx.camera:camera-lifecycle:1.0.0-beta12")
  implementation("androidx.camera:camera-view:1.0.0-alpha19")

  bundledMLImplementation("com.google.mlkit:barcode-scanning:16.0.3")
  unbundledMLImplementation("com.google.android.gms:play-services-mlkit-barcode-scanning:16.1.2")
}
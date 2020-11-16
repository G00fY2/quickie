plugins {
  id("com.android.application")
  kotlin("android")
}

android {
  compileSdkVersion(30)
  buildToolsVersion = "30.0.2"
  defaultConfig {
    applicationId = "com.g00fy2.quickiesample"
    minSdkVersion(21)
    targetSdkVersion(30)
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
  flavorDimensions("bundleMode")
  productFlavors {
    create("bundledML").dimension("bundleMode")
    create("unbundledML").dimension("bundleMode")
  }
  buildFeatures {
    viewBinding = true
    aidl = false
    renderScript = false
    resValues = false
    shaders = false
  }
  sourceSets {
    getByName("main").java.srcDirs("src/main/kotlin")
  }
  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
  }
  kotlinOptions {
    jvmTarget = JavaVersion.VERSION_1_8.toString()
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

dependencies {
  implementation(project(":quickie"))

  implementation("androidx.appcompat:appcompat:1.3.0-alpha02")

  implementation("com.google.android.material:material:1.2.1")
  implementation("androidx.constraintlayout:constraintlayout:2.0.4")
}
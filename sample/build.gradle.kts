plugins {
  id("com.android.application")
  kotlin("android")
}

android {
  compileSdkVersion(Config.androidCompileSdkVersion)
  buildToolsVersion = Config.buildToolsVersion
  defaultConfig {
    applicationId = Config.applicationId
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
  flavorDimensions("bundleMode")
  productFlavors {
    create("bundledML") {
      dimension("bundleMode")
    }
    create("unbundledML") {
      dimension("bundleMode")
    }
  }
  buildFeatures {
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
  kotlinOptions {
    jvmTarget = JavaVersion.VERSION_1_8.toString()
  }
}

repositories {
  google()
  mavenCentral()
  jcenter()
}

dependencies {
  implementation(project(":quickie"))

  implementation(Deps.AndroidX.appcompat)
  implementation(Deps.Mdc.materialDesign)
}
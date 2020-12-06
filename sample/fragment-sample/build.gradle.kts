plugins {
  id(Plugins.Android.application)
  id(Plugins.Kotlin.android)
}

android {
  compileSdkVersion(Config.androidCompileSdkVersion)
  buildToolsVersion = Config.buildToolsVersion
  defaultConfig {
    applicationId = Config.fragmentSampleAppId
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
  splits {
    abi {
      isEnable = true
      reset()
      include("x86", "armeabi-v7a", "arm64-v8a", "x86_64")
      isUniversalApk = true
    }
  }
  flavorDimensions("mlkit")
  productFlavors {
    create("bundled").dimension("mlkit")
    create("unbundled").dimension("mlkit")
  }
  buildFeatures {
    viewBinding = true
    aidl = false
    renderScript = false
    resValues = false
    shaders = false
  }
  sourceSets.getByName("main").java.srcDirs("src/main/kotlin")
}

repositories {
  google()
  mavenCentral()
  jcenter()
}

dependencies {
  implementation(project(":quickie"))

  implementation(Deps.AndroidX.appcompat)
  implementation(Deps.AndroidX.navigation)
  implementation(Deps.Mdc.materialDesign)
}
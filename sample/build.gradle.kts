plugins {
  alias(libs.plugins.android.application)
  alias(libs.plugins.kotlin.android)
}

android {
  namespace = "io.github.g00fy2.quickiesample"
  defaultConfig {
    applicationId = "io.github.g00fy2.quickiesample"
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
  flavorDimensions += "mlkit"
  productFlavors {
    create("bundled").dimension = "mlkit"
    create("unbundled").dimension = "mlkit"
  }
  buildFeatures {
    buildConfig = true
    viewBinding = true
  }
  lint {
    abortOnError = true
    warningsAsErrors = true
    checkDependencies = true
    disable.addAll(
      listOf(
        "RtlEnabled",
        "GradleDependency",
        "AndroidGradlePluginVersion",
        "OldTargetApi",
      )
    )
  }
}

dependencies {
  implementation(project(":quickie"))

  implementation(libs.google.materialDesign)
}
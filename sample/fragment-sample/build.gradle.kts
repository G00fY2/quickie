plugins {
  id(Plugins.sampleApp)
}

android {
  defaultConfig.applicationId = Config.fragmentSampleAppId

  kotlinOptions {
    jvmTarget = JavaVersion.VERSION_1_8.toString()
  }
}

dependencies {
  implementation(Deps.AndroidX.navigation)
}
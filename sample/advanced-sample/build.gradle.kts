plugins {
  id(Plugins.sampleApp)
}

android {
  defaultConfig.applicationId = Config.advancedSampleAppId

  kotlinOptions {
    jvmTarget = JavaVersion.VERSION_1_8.toString()
  }
}
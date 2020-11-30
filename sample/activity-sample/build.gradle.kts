plugins {
  id(Plugins.sampleApp)
}

android {
  defaultConfig.applicationId = Config.activitySampleAppId

  kotlinOptions {
    jvmTarget = JavaVersion.VERSION_1_8.toString()
  }
}
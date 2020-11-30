plugins {
  id(Plugins.sampleApp)
}

android {
  defaultConfig.applicationId = Config.fragmentSampleAppId
}

dependencies {
  implementation(Deps.AndroidX.navigation)
}
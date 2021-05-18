object Versions {

  const val androidMinSdk = 21
  const val androidCompileSdk = 30
  const val androidTargetSdk = 30
  const val androidBuildTools = "30.0.3"

  const val androidGradle = "4.2.1"
  const val kotlin = "1.5.0"

  const val appcompat = "1.3.0-rc01"

  const val cameraX = "1.0.0"
  const val cameraView = "1.0.0-alpha24"

  const val materialDesign = "1.3.0"

  const val barcodeScanning = "16.1.1"
  const val barcodeScanningGms = "16.1.4"

  const val detekt = "1.17.0"
  const val gradleVersions = "0.38.0"
  const val dokka = "1.4.32"

  fun maturityLevel(version: String): Int {
    val levels = listOf("alpha", "beta", "m", "rc")
    levels.forEachIndexed { index, s ->
      if (version.matches(".*[.\\-]$s[.\\-\\d]*".toRegex(RegexOption.IGNORE_CASE))) return index
    }
    return levels.size
  }
}
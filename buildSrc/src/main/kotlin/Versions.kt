object Versions {

  const val androidMinSdk = 21
  const val androidCompileSdk = 30
  const val androidTargetSdk = 30
  const val androidBuildTools = "30.0.3"

  const val androidGradle = "4.2.1"
  const val kotlin = "1.5.20"

  const val appcompat = "1.3.0"

  const val cameraX = "1.0.0"
  const val cameraView = "1.0.0-alpha25"

  const val barcodeScanning = "16.2.0"
  const val barcodeScanningGms = "16.2.0"

  const val materialDesign = "1.3.0"

  const val detekt = "1.17.1"
  const val gradleVersions = "0.39.0"
  const val dokka = "1.4.32"

  const val junit = "5.7.2"

  fun maturityLevel(version: String): Int {
    val levels = listOf("alpha", "beta", "m", "rc")
    levels.forEachIndexed { index, s ->
      if (version.matches(".*[.\\-]$s[.\\-\\d]*".toRegex(RegexOption.IGNORE_CASE))) return index
    }
    return levels.size
  }
}
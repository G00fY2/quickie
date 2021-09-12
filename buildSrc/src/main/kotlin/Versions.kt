object Versions {

  const val androidMinSdk = 21
  const val androidCompileSdk = 30
  const val androidTargetSdk = 30
  const val androidBuildTools = "30.0.3"

  object Preview {
    const val androidCompileSdk = 31
    const val androidTargetSdk = 31
    const val androidBuildTools = "31.0.0"
  }

  const val androidGradle = "7.0.2"
  const val kotlin = "1.5.30"

  const val appcompat = "1.3.1"

  const val cameraX = "1.0.1"
  const val cameraView = "1.0.0-alpha28"

  const val barcodeScanning = "17.0.0"
  const val barcodeScanningGms = "16.2.1"

  const val materialDesign = "1.4.0"

  const val detekt = "1.18.1"
  const val gradleVersions = "0.39.0"
  const val dokka = "1.5.0"

  const val junit = "5.8.0"

  fun maturityLevel(version: String): Int {
    val levels = listOf("alpha", "beta", "m", "rc")
    levels.forEachIndexed { index, s ->
      if (version.matches(".*[.\\-]$s[.\\-\\d]*".toRegex(RegexOption.IGNORE_CASE))) return index
    }
    return levels.size
  }
}
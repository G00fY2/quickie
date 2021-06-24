object Deps {

  object AndroidX {
    const val appcompat = "androidx.appcompat:appcompat:${Versions.appcompat}"

    const val camera = "androidx.camera:camera-camera2:${Versions.cameraX}"
    const val cameraLifecycle = "androidx.camera:camera-lifecycle:${Versions.cameraX}"
    const val cameraPreview = "androidx.camera:camera-view:${Versions.cameraView}"
  }

  object MLKit {
    const val barcodeScanning = "com.google.mlkit:barcode-scanning:${Versions.barcodeScanning}"
    const val barcodeScanningGms =
      "com.google.android.gms:play-services-mlkit-barcode-scanning:${Versions.barcodeScanningGms}"
  }

  object UI {
    const val materialDesign = "com.google.android.material:material:${Versions.materialDesign}"
  }

  object Test {
    const val junitApi = "org.junit.jupiter:junit-jupiter-api:${Versions.junit}"
    const val junitEngine = "org.junit.jupiter:junit-jupiter-engine:${Versions.junit}"
  }
}
object Deps {

  object AndroidX {
    const val activity = "androidx.activity:activity:${Versions.activity}"
    const val fragment = "androidx.fragment:fragment:${Versions.fragment}"
    const val appcompat = "androidx.appcompat:appcompat:${Versions.appcompat}"
    const val core = "androidx.core:core:${Versions.core}"

    const val camera = "androidx.camera:camera-camera2:${Versions.cameraX}"
    const val cameraLifecycle = "androidx.camera:camera-lifecycle:${Versions.cameraX}"
    const val cameraPreview = "androidx.camera:camera-view:${Versions.cameraView}"

    const val navigation = "androidx.navigation:navigation-fragment-ktx:${Versions.navigation}"
  }

  object Mdc {
    const val materialDesign = "com.google.android.material:material:${Versions.materialDesign}"
  }

  object MLKit {
    const val barcodeScanning = "com.google.mlkit:barcode-scanning:${Versions.barcodeScanning}"
    const val barcodeScanningGms =
      "com.google.android.gms:play-services-mlkit-barcode-scanning:${Versions.barcodeScanningGms}"
  }
}
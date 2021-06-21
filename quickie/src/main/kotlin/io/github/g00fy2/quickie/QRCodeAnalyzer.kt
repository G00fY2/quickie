package io.github.g00fy2.quickie

import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.barcode.Barcode
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage

internal class QRCodeAnalyzer(
  private val barcodeFormats: IntArray,
  private val onSuccess: ((Barcode) -> Unit),
  private val onFailure: ((Exception) -> Unit),
  private val onImageAnalyzed: ((Boolean) -> Unit)
) : ImageAnalysis.Analyzer {

  private val barcodeScanner by lazy {
    val optionsBuilder = if (barcodeFormats.size > 1) {
      BarcodeScannerOptions.Builder().setBarcodeFormats(barcodeFormats.first(), *barcodeFormats.drop(1).toIntArray())
    } else {
      BarcodeScannerOptions.Builder().setBarcodeFormats(barcodeFormats.firstOrNull() ?: Barcode.FORMAT_UNKNOWN)
    }
    BarcodeScanning.getClient(optionsBuilder.build())
  }
  private var throttleAnalysis = false
  private var lastAnalysisTime = 0L

  @ExperimentalGetImage
  override fun analyze(imageProxy: ImageProxy) {
    if (imageProxy.image == null) return

    if (throttleAnalysis && System.currentTimeMillis() - lastAnalysisTime < THROTTLE_RATE_MS) {
      imageProxy.close()
      return
    }

    var errorOccured = false
    barcodeScanner.process(imageProxy.toInputImage())
      .addOnSuccessListener { codes -> codes.mapNotNull { it }.firstOrNull()?.let { onSuccess(it) } }
      .addOnFailureListener {
        errorOccured = true
        onFailure(it)
      }
      .addOnCompleteListener {
        lastAnalysisTime = System.currentTimeMillis()
        throttleAnalysis = errorOccured
        onImageAnalyzed(errorOccured)
        imageProxy.close()
      }
  }

  @ExperimentalGetImage
  private fun ImageProxy.toInputImage() = InputImage.fromMediaImage(image!!, imageInfo.rotationDegrees)

  companion object {
    private const val THROTTLE_RATE_MS = 1000L
  }
}
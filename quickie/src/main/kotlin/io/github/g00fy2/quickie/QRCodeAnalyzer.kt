package io.github.g00fy2.quickie

import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage

internal class QRCodeAnalyzer(
  private val barcodeFormats: IntArray,
  private val onSuccess: ((Barcode) -> Unit),
  private val onFailure: ((Exception) -> Unit),
  private val onPassCompleted: ((Boolean) -> Unit)
) : ImageAnalysis.Analyzer {

  private val barcodeScanner by lazy {
    val optionsBuilder = if (barcodeFormats.size > 1) {
      BarcodeScannerOptions.Builder().setBarcodeFormats(barcodeFormats.first(), *barcodeFormats.drop(1).toIntArray())
    } else {
      BarcodeScannerOptions.Builder().setBarcodeFormats(barcodeFormats.firstOrNull() ?: Barcode.FORMAT_UNKNOWN)
    }
    try {
      BarcodeScanning.getClient(optionsBuilder.build())
    } catch (e: Exception) { // catch if for some reason MlKitContext has not been initialized
      onFailure(e)
      null
    }
  }

  private var failureOccurred = false
  private var failureTimestamp = 0L

  @ExperimentalGetImage
  override fun analyze(imageProxy: ImageProxy) {
    val mediaImage = imageProxy.image

    // skip analysis if no media image or error occurred in previous pass
    if (mediaImage == null || (failureOccurred && System.currentTimeMillis() - failureTimestamp < 1000L)) {
      imageProxy.close()
      return
    }

    failureOccurred = false
    barcodeScanner?.let { scanner ->
      scanner.process(InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees))
        .addOnSuccessListener { codes -> codes.firstNotNullOfOrNull { it }?.let { onSuccess(it) } }
        .addOnFailureListener {
          failureOccurred = true
          failureTimestamp = System.currentTimeMillis()
          onFailure(it)
        }
        .addOnCompleteListener {
          onPassCompleted(failureOccurred)
          imageProxy.close()
        }
    }
  }
}
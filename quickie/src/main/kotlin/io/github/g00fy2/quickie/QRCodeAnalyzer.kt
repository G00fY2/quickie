package io.github.g00fy2.quickie

import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.android.gms.tasks.Task
import com.google.mlkit.vision.barcode.Barcode
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage

@ExperimentalGetImage
internal class QRCodeAnalyzer(
  private val barcodeFormats: IntArray,
  private val onSuccess: ((Barcode) -> Unit),
  private val onFailure: ((Exception) -> Unit)
) : ImageAnalysis.Analyzer {

  private var pendingTask: Task<List<Barcode>>? = null
  private val barcodeScanner by lazy {
    val optionsBuilder = if (barcodeFormats.size > 1) {
      BarcodeScannerOptions.Builder().setBarcodeFormats(barcodeFormats.first(), *barcodeFormats.drop(1).toIntArray())
    } else {
      BarcodeScannerOptions.Builder().setBarcodeFormats(barcodeFormats.firstOrNull() ?: Barcode.FORMAT_UNKNOWN)
    }
    BarcodeScanning.getClient(optionsBuilder.build())
  }

  override fun analyze(imageProxy: ImageProxy) {
    if (pendingTask?.isComplete == false || imageProxy.image == null) return

    pendingTask = barcodeScanner.process(imageProxy.toInputImage())
      .addOnSuccessListener { codes -> codes.mapNotNull { it }.firstOrNull()?.let { onSuccess(it) } }
      .addOnFailureListener { onFailure(it) }
      .addOnCompleteListener { imageProxy.close() }
  }

  private fun ImageProxy.toInputImage() = InputImage.fromMediaImage(image!!, imageInfo.rotationDegrees)
}
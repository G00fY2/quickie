package io.github.g00fy2.quickie

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Size
import android.view.HapticFeedbackConstants
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ContextThemeWrapper
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import com.google.mlkit.vision.barcode.Barcode
import io.github.g00fy2.quickie.config.ParcelableScannerConfig
import io.github.g00fy2.quickie.databinding.QuickieScannerActivityBinding
import io.github.g00fy2.quickie.extensions.toParcelableContentType
import io.github.g00fy2.quickie.utils.PlayServicesValidator
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

internal class QRScannerActivity : AppCompatActivity() {

  private lateinit var binding: QuickieScannerActivityBinding
  private lateinit var analysisExecutor: ExecutorService
  private var barcodeFormats = intArrayOf(Barcode.FORMAT_QR_CODE)

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    val appThemeLayoutInflater = applicationInfo.theme.let { appThemeRes ->
      if (appThemeRes != 0) layoutInflater.cloneInContext(ContextThemeWrapper(this, appThemeRes)) else layoutInflater
    }
    binding = QuickieScannerActivityBinding.inflate(appThemeLayoutInflater)
    setContentView(binding.root)

    setupEdgeToEdgeUI()
    applyScannerConfig()

    analysisExecutor = Executors.newSingleThreadExecutor()

    requestCameraPermissionIfMissing { granted ->
      if (granted) {
        startCamera()
      } else {
        setResult(RESULT_MISSING_PERMISSION, null)
        finish()
      }
    }
  }

  override fun onDestroy() {
    super.onDestroy()
    analysisExecutor.shutdown()
  }

  private fun startCamera() {
    val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

    cameraProviderFuture.addListener({
      val cameraProvider = cameraProviderFuture.get()

      val preview = Preview.Builder().build().also { it.setSurfaceProvider(binding.previewView.surfaceProvider) }
      val imageAnalysis = ImageAnalysis.Builder()
        .setTargetResolution(Size(1280, 720))
        .build()
        .also {
          it.setAnalyzer(analysisExecutor,
            QRCodeAnalyzer(
              barcodeFormats,
              { barcode ->
                it.clearAnalyzer()
                onSuccess(barcode)
              },
              { exception ->
                it.clearAnalyzer()
                onFailure(exception)
              }
            )
          )
        }

      cameraProvider.unbindAll()
      try {
        cameraProvider.bindToLifecycle(this, CameraSelector.DEFAULT_BACK_CAMERA, preview, imageAnalysis)
        binding.overlayView.visibility = View.VISIBLE
      } catch (e: Exception) {
        onFailure(e)
      }
    }, ContextCompat.getMainExecutor(this))
  }

  private fun onSuccess(result: Barcode) {
    binding.overlayView.isHighlighted = true
    binding.overlayView.performHapticFeedback(
      HapticFeedbackConstants.KEYBOARD_TAP,
      HapticFeedbackConstants.FLAG_IGNORE_VIEW_SETTING or HapticFeedbackConstants.FLAG_IGNORE_GLOBAL_SETTING
    )
    setResult(
      Activity.RESULT_OK,
      Intent().apply {
        putExtra(EXTRA_RESULT_VALUE, result.rawValue)
        putExtra(EXTRA_RESULT_TYPE, result.valueType)
        putExtra(EXTRA_RESULT_PARCELABLE, result.toParcelableContentType())
      }
    )
    finish()
  }

  private fun onFailure(exception: Exception) {
    setResult(RESULT_ERROR, Intent().putExtra(EXTRA_RESULT_EXCEPTION, exception))
    if (!PlayServicesValidator.handleGooglePlayServicesError(this, exception)) finish()
  }

  private fun setupEdgeToEdgeUI() {
    WindowCompat.setDecorFitsSystemWindows(window, false)
    ViewCompat.setOnApplyWindowInsetsListener(binding.overlayView) { v, insets ->
      insets.getInsets(WindowInsetsCompat.Type.systemBars()).let {
        v.setPadding(it.left, it.top, it.right, it.bottom)
      }
      WindowInsetsCompat.CONSUMED
    }
  }

  private fun applyScannerConfig() {
    intent?.getParcelableExtra<ParcelableScannerConfig>(EXTRA_CONFIG)?.let {
      barcodeFormats = it.formats
      binding.overlayView.setCustomTextAndIcon(it.stringRes, it.drawableRes)
    }
  }

  private fun requestCameraPermissionIfMissing(onResult: ((Boolean) -> Unit)) {
    if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
      onResult(true)
    } else {
      // register the activity result here is allowed since we call this in onCreate only
      registerForActivityResult(ActivityResultContracts.RequestPermission()) {
        onResult(it)
      }.launch(Manifest.permission.CAMERA)
    }
  }

  companion object {
    const val EXTRA_CONFIG = "quickie-config"
    const val EXTRA_RESULT_VALUE = "quickie-value"
    const val EXTRA_RESULT_TYPE = "quickie-type"
    const val EXTRA_RESULT_PARCELABLE = "quickie-parcelable"
    const val EXTRA_RESULT_EXCEPTION = "quickie-exception"
    const val RESULT_MISSING_PERMISSION = RESULT_FIRST_USER + 1
    const val RESULT_ERROR = RESULT_FIRST_USER + 2
  }
}
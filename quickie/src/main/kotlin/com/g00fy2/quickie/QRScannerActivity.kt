package com.g00fy2.quickie

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.util.Size
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import com.g00fy2.quickie.databinding.ActivityScannerBinding
import com.g00fy2.quickie.extensions.toParcelableContentType
import com.google.mlkit.vision.barcode.Barcode
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@ExperimentalGetImage
class QRScannerActivity : ComponentActivity() {

  private lateinit var binding: ActivityScannerBinding
  private lateinit var cameraExecutor: ExecutorService

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityScannerBinding.inflate(layoutInflater)
    setContentView(binding.root)

    setupEdgeToEdgeUI()

    cameraExecutor = Executors.newSingleThreadExecutor()

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
    cameraExecutor.shutdown()
  }

  private fun startCamera() {
    val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

    cameraProviderFuture.addListener(
      { setupUseCases(cameraProviderFuture.get()) },
      ContextCompat.getMainExecutor(this)
    )
  }

  private fun setupUseCases(cameraProvider: ProcessCameraProvider) {
    val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
    val imageAnalysis = ImageAnalysis.Builder()
      .setTargetResolution(Size(1280, 720))
      .build()
      .apply { setAnalyzer(cameraExecutor, QRCodeAnalyzer({ onSuccess(it) }, { onFailure(it) })) }
    val preview = Preview.Builder().build()

    cameraProvider.unbindAll()
    try {
      cameraProvider.bindToLifecycle(this, cameraSelector, imageAnalysis, preview)
      preview.setSurfaceProvider(binding.previewView.surfaceProvider)
      binding.decorationView.visibility = View.VISIBLE
    } catch (e: Exception) {
      onFailure(e)
    }
  }

  private fun onSuccess(result: Barcode) {
    binding.decorationView.isHighlighted = true
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
    Log.e(localClassName, exception.message, exception)
    finish()
  }

  private fun setupEdgeToEdgeUI() {
    // TODO migrate to androidx.core:core:1.5.0 once stable
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
      window.setDecorFitsSystemWindows(false)
    } else {
      window.decorView.let {
        @Suppress("DEPRECATION")
        it.systemUiVisibility.let { flags ->
          it.systemUiVisibility = (
            flags
              or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
              or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
              or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            )
        }
      }
    }
    ViewCompat.setOnApplyWindowInsetsListener(binding.decorationView) { v, insets ->
      insets.systemWindowInsets.let {
        v.setPadding(it.left, it.top, it.right, it.bottom)
      }
      insets
    }
  }

  private fun requestCameraPermissionIfMissing(onResult: ((Boolean) -> Unit)) {
    if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
      onResult(true)
    } else {
      registerForActivityResult(ActivityResultContracts.RequestPermission()) {
        onResult(it)
      }.launch(Manifest.permission.CAMERA)
    }
  }

  companion object {
    const val EXTRA_RESULT_VALUE = "quickie-value"
    const val EXTRA_RESULT_TYPE = "quickie-type"
    const val EXTRA_RESULT_PARCELABLE = "quickie-parcelable"
    const val EXTRA_RESULT_EXCEPTION = "quickie-exception"
    const val RESULT_MISSING_PERMISSION = RESULT_FIRST_USER + 1
    const val RESULT_ERROR = RESULT_FIRST_USER + 2
  }
}
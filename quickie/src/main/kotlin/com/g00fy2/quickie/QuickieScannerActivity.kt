package com.g00fy2.quickie

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.util.Size
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import com.g00fy2.quickie.databinding.ActivityScannerBinding
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@ExperimentalGetImage
class QuickieScannerActivity : AppCompatActivity() {

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
        setResult(RESULT_CANCELED, null)
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

  private fun onSuccess(result: String) {
    binding.decorationView.isHighlighted = true
    setResult(Activity.RESULT_OK, Intent().putExtra(EXTRA_DATA, result))
    finish()
  }

  private fun onFailure(exception: Exception) {
    setResult(RESULT_CANCELED, null)
    Log.e(localClassName, exception.message, exception)
    finish()
  }

  private fun setupEdgeToEdgeUI() {
    WindowCompat.setDecorFitsSystemWindows(window, false)
    ViewCompat.setOnApplyWindowInsetsListener(binding.decorationView) { v, insets ->
      insets.getInsets(WindowInsetsCompat.Type.systemBars()).let {
        v.setPadding(it.left, it.top, it.right, it.bottom)
      }
      insets
    }
  }

  private fun AppCompatActivity.requestCameraPermissionIfMissing(onResult: ((Boolean) -> Unit)) {
    if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
      onResult(true)
    } else {
      registerForActivityResult(ActivityResultContracts.RequestPermission()) {
        onResult(it)
      }.launch(Manifest.permission.CAMERA)
    }
  }

  companion object {
    const val EXTRA_DATA = "quickie-data"
  }
}
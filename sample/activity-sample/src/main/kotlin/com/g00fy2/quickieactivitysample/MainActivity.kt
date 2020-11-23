package com.g00fy2.quickieactivitysample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.g00fy2.quickie.QRResult
import com.g00fy2.quickie.QRResult.QRError
import com.g00fy2.quickie.QRResult.QRMissingPermission
import com.g00fy2.quickie.QRResult.QRSuccess
import com.g00fy2.quickie.QRResult.QRUserCanceled
import com.g00fy2.quickie.ScanQRCode
import com.g00fy2.quickieactivitysample.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

  private lateinit var binding: ActivityMainBinding
  private var snackbar: Snackbar? = null

  private val scanQrCode = registerForActivityResult(ScanQRCode()) {
    showSnackbar(it)
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityMainBinding.inflate(layoutInflater)
    setContentView(binding.root)

    binding.buttonQrScanner.setOnClickListener {
      snackbar?.dismiss()
      scanQrCode.launch(null)
    }
  }

  private fun showSnackbar(result: QRResult) {
    val text = when (result) {
      is QRSuccess -> result.content.rawValue
      QRUserCanceled -> "User canceled"
      QRMissingPermission -> "Missing permission"
      is QRError -> "${result.exception.javaClass.simpleName}: ${result.exception.localizedMessage}"
    }

    snackbar = Snackbar.make(binding.root, text, Snackbar.LENGTH_INDEFINITE).apply {
      setAction(android.R.string.ok) { }
    }
    snackbar?.show()
  }
}
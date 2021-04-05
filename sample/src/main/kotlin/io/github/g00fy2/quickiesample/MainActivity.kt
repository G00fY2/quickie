package io.github.g00fy2.quickiesample

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import io.github.g00fy2.quickie.QRResult
import io.github.g00fy2.quickie.QRResult.QRError
import io.github.g00fy2.quickie.QRResult.QRMissingPermission
import io.github.g00fy2.quickie.QRResult.QRSuccess
import io.github.g00fy2.quickie.QRResult.QRUserCanceled
import io.github.g00fy2.quickie.ScanQRCode
import io.github.g00fy2.quickie.content.QRContent
import io.github.g00fy2.quickiesample.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

  private lateinit var binding: ActivityMainBinding
  private var snackbar: Snackbar? = null

  private val scanQrCode = registerForActivityResult(ScanQRCode(), ::showSnackbar)

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
      view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)?.maxLines = 5
      if (result is QRSuccess && result.content is QRContent.Url) {
        setAction(R.string.open_action) { openUrl(result.content.rawValue) }
      } else {
        setAction(R.string.ok_action) { }
      }
    }
    snackbar?.show()
  }

  private fun openUrl(url: String) {
    try {
      startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
    } catch (ignored: ActivityNotFoundException) {
      // no Activity found to run the given Intent
    }
  }
}
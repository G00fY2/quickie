package com.g00fy2.quickiesample

import android.content.ClipData
import android.content.ClipboardManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.g00fy2.quickiesample.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

  private lateinit var binding: ActivityMainBinding

  private val viewModel: MainViewModel by lazy {
    ViewModelProvider(this).get(MainViewModel::class.java)
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityMainBinding.inflate(layoutInflater)
    setContentView(binding.root)

    viewModel.registerQrScanLauncher(activityResultRegistry)

    binding.buttonQrScanner.setOnClickListener {
      viewModel.getQrCode()
    }

    viewModel.qrCodeState.observe(this) { showSnackbar(it) }
  }

  private fun showSnackbar(qrCode: String?) {
    Snackbar.make(binding.root, qrCode ?: "Canceled", Snackbar.LENGTH_LONG).apply {
      if (qrCode != null) {
        setAction(R.string.copy_to_clipboard) {
          ContextCompat.getSystemService(this@MainActivity, ClipboardManager::class.java)
            ?.setPrimaryClip(ClipData.newPlainText("data", qrCode))
        }
      }
    }.show()
  }
}
package com.g00fy2.quickiesample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.g00fy2.quickie.QuickieResult
import com.g00fy2.quickie.QuickieResult.QuickieError
import com.g00fy2.quickie.QuickieResult.QuickieMissingPermission
import com.g00fy2.quickie.QuickieResult.QuickieSuccess
import com.g00fy2.quickie.QuickieResult.QuickieUserCancel
import com.g00fy2.quickiesample.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

  private lateinit var binding: ActivityMainBinding
  private var snackbar: Snackbar? = null

  private val viewModel: MainViewModel by lazy {
    ViewModelProvider(this).get(MainViewModel::class.java)
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityMainBinding.inflate(layoutInflater)
    setContentView(binding.root)

    viewModel.registerQrScanLauncher(activityResultRegistry)

    binding.buttonQrScanner.setOnClickListener {
      snackbar?.dismiss()
      viewModel.getQrCode()
    }

    viewModel.qrCodeState.observe(this) { showSnackbar(it) }
  }

  private fun showSnackbar(result: QuickieResult) {
    val text = when (result) {
      is QuickieSuccess -> result.content
      QuickieUserCancel -> "User canceled"
      QuickieMissingPermission -> "Missing permission"
      is QuickieError -> "${result.exception.javaClass.simpleName}: ${result.exception.localizedMessage}"
    }

    snackbar = Snackbar.make(binding.root, text, Snackbar.LENGTH_INDEFINITE).apply {
      setAction(android.R.string.ok) { }
    }
    snackbar?.show()
  }
}
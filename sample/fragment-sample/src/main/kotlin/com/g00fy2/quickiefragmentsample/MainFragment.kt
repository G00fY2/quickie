package com.g00fy2.quickiefragmentsample

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.g00fy2.quickie.QRResult
import com.g00fy2.quickie.QRResult.QRError
import com.g00fy2.quickie.QRResult.QRMissingPermission
import com.g00fy2.quickie.QRResult.QRSuccess
import com.g00fy2.quickie.QRResult.QRUserCanceled
import com.g00fy2.quickie.ScanQRCode
import com.g00fy2.quickiefragmentsample.databinding.FragmentMainBinding
import com.google.android.material.snackbar.Snackbar

class MainFragment : Fragment() {

  private var _binding: FragmentMainBinding? = null
  private val binding get() = _binding!!
  private var snackbar: Snackbar? = null

  private val scanQrCode = registerForActivityResult(ScanQRCode(), ::showSnackbar)

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
    _binding = FragmentMainBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    binding.buttonQrScanner.setOnClickListener {
      snackbar?.dismiss()
      scanQrCode.launch(null)
    }
  }

  override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
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
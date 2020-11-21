package com.g00fy2.quickiesample

import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.ActivityResultRegistry
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.g00fy2.quickie.QRResult
import com.g00fy2.quickie.ScanQRCode
import com.g00fy2.quickiesample.livedata.SingleLiveEvent

class MainViewModel : ViewModel() {

  private lateinit var scanQRCodeLauncher: ActivityResultLauncher<Nothing?>
  private val _qrCodeState = SingleLiveEvent<QRResult>()
  val qrCodeState: LiveData<QRResult> = _qrCodeState

  // TODO consider injecting the registry
  fun registerQrScanLauncher(registry: ActivityResultRegistry) {
    scanQRCodeLauncher = registry.register("key", ScanQRCode()) { qrResult ->
      _qrCodeState.value = qrResult
    }
  }

  fun scanQRCode() {
    scanQRCodeLauncher.launch(null)
  }

  override fun onCleared() {
    scanQRCodeLauncher.unregister()
  }
}
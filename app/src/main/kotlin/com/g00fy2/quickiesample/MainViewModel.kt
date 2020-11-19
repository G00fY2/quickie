package com.g00fy2.quickiesample

import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.ActivityResultRegistry
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.g00fy2.quickie.QuickieScan
import com.g00fy2.quickiesample.livedata.SingleLiveEvent

class MainViewModel : ViewModel() {

  private lateinit var getQrCode: ActivityResultLauncher<Nothing?>
  private val _qrCodeState = SingleLiveEvent<String?>()
  val qrCodeState: LiveData<String?> = _qrCodeState

  // TODO consider injecting the registry
  fun registerQrScanLauncher(registry: ActivityResultRegistry) {
    getQrCode = registry.register("key", QuickieScan()) { qrResult ->
      _qrCodeState.value = qrResult
    }
  }

  fun getQrCode() {
    getQrCode.launch(null)
  }

  override fun onCleared() {
    getQrCode.unregister()
  }
}
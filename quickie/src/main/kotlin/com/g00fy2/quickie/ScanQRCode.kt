package com.g00fy2.quickie

import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import com.g00fy2.quickie.QRResult.QRError
import com.g00fy2.quickie.QRResult.QRMissingPermission
import com.g00fy2.quickie.QRResult.QRSuccess
import com.g00fy2.quickie.QRResult.QRUserCanceled
import com.g00fy2.quickie.QRScannerActivity.Companion.EXTRA_RESULT_DATA
import com.g00fy2.quickie.QRScannerActivity.Companion.EXTRA_RESULT_EXCEPTION
import com.g00fy2.quickie.QRScannerActivity.Companion.RESULT_ERROR
import com.g00fy2.quickie.QRScannerActivity.Companion.RESULT_MISSING_PERMISSION

class ScanQRCode : ActivityResultContract<Nothing?, QRResult>() {

  override fun createIntent(context: Context, input: Nothing?): Intent =
    Intent(context, QRScannerActivity::class.java)

  override fun parseResult(resultCode: Int, intent: Intent?): QRResult {
    return when (resultCode) {
      RESULT_OK -> QRSuccess(intent?.getStringExtra(EXTRA_RESULT_DATA) ?: "")
      RESULT_CANCELED -> QRUserCanceled
      RESULT_MISSING_PERMISSION -> QRMissingPermission
      RESULT_ERROR -> {
        try {
          intent?.getSerializableExtra(EXTRA_RESULT_EXCEPTION) as Exception
        } catch (e: Exception) {
          IllegalStateException("Could retrieve root exception")
        }.let { QRError(it) }
      }
      else -> QRError(IllegalStateException("Unknown result code $resultCode"))
    }
  }
}
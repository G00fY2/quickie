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
import com.g00fy2.quickie.QRScannerActivity.Companion.RESULT_ERROR
import com.g00fy2.quickie.QRScannerActivity.Companion.RESULT_MISSING_PERMISSION
import com.g00fy2.quickie.extensions.getRootException
import com.g00fy2.quickie.extensions.toQuickieContentType

public class ScanQRCode : ActivityResultContract<Nothing?, QRResult>() {

  override fun createIntent(context: Context, input: Nothing?): Intent =
    Intent(context, QRScannerActivity::class.java)

  override fun parseResult(resultCode: Int, intent: Intent?): QRResult {
    return when (resultCode) {
      RESULT_OK -> QRSuccess(intent.toQuickieContentType())
      RESULT_CANCELED -> QRUserCanceled
      RESULT_MISSING_PERMISSION -> QRMissingPermission
      RESULT_ERROR -> QRError(intent.getRootException())
      else -> QRError(IllegalStateException("Unknown activity result code $resultCode"))
    }
  }
}
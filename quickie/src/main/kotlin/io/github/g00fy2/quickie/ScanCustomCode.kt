package io.github.g00fy2.quickie

import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import io.github.g00fy2.quickie.QRResult.QRError
import io.github.g00fy2.quickie.QRResult.QRMissingPermission
import io.github.g00fy2.quickie.QRResult.QRSuccess
import io.github.g00fy2.quickie.QRResult.QRUserCanceled
import io.github.g00fy2.quickie.QRScannerActivity.Companion.EXTRA_CONFIG
import io.github.g00fy2.quickie.QRScannerActivity.Companion.RESULT_ERROR
import io.github.g00fy2.quickie.QRScannerActivity.Companion.RESULT_MISSING_PERMISSION
import io.github.g00fy2.quickie.config.ScannerConfig
import io.github.g00fy2.quickie.extensions.getRootException
import io.github.g00fy2.quickie.extensions.toParcelableConfig
import io.github.g00fy2.quickie.extensions.toQuickieContentType

public class ScanCustomCode : ActivityResultContract<ScannerConfig, QRResult>() {

  override fun createIntent(context: Context, input: ScannerConfig): Intent {
    return Intent(context, QRScannerActivity::class.java).apply {
      putExtra(EXTRA_CONFIG, input.toParcelableConfig())
    }
  }

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
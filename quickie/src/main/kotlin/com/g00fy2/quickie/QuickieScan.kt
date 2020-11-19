package com.g00fy2.quickie

import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import com.g00fy2.quickie.QuickieResult.QuickieError
import com.g00fy2.quickie.QuickieResult.QuickieMissingPermission
import com.g00fy2.quickie.QuickieResult.QuickieSuccess
import com.g00fy2.quickie.QuickieResult.QuickieUserCancel
import com.g00fy2.quickie.QuickieScannerActivity.Companion.EXTRA_RESULT_DATA
import com.g00fy2.quickie.QuickieScannerActivity.Companion.EXTRA_RESULT_EXCEPTION
import com.g00fy2.quickie.QuickieScannerActivity.Companion.RESULT_ERROR
import com.g00fy2.quickie.QuickieScannerActivity.Companion.RESULT_MISSING_PERMISSION

class QuickieScan : ActivityResultContract<Nothing?, QuickieResult>() {

  override fun createIntent(context: Context, input: Nothing?): Intent =
    Intent(context, QuickieScannerActivity::class.java)

  override fun parseResult(resultCode: Int, intent: Intent?): QuickieResult {
    return when (resultCode) {
      RESULT_OK -> QuickieSuccess(intent?.getStringExtra(EXTRA_RESULT_DATA) ?: "")
      RESULT_CANCELED -> QuickieUserCancel
      RESULT_MISSING_PERMISSION -> QuickieMissingPermission
      RESULT_ERROR -> {
        try {
          intent?.getSerializableExtra(EXTRA_RESULT_EXCEPTION) as Exception
        } catch (e: Exception) {
          IllegalStateException("Could retrieve root exception")
        }.let { QuickieError(it) }
      }
      else -> QuickieError(IllegalStateException("Unknown result code $resultCode"))
    }
  }
}
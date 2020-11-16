package com.g00fy2.quickie

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import com.g00fy2.quickie.QuickieScannerActivity.Companion.EXTRA_DATA

class QuickieScan : ActivityResultContract<Nothing?, String?>() {

  override fun createIntent(context: Context, input: Nothing?): Intent =
    Intent(context, QuickieScannerActivity::class.java)

  override fun parseResult(resultCode: Int, intent: Intent?): String? =
    if (resultCode == RESULT_OK) intent?.getStringExtra(EXTRA_DATA) else null
}
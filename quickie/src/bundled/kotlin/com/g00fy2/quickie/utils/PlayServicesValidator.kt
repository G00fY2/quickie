package com.g00fy2.quickie.utils

import android.app.Activity
import java.lang.Exception

object PlayServicesValidator {

  @Suppress("UNUSED_PARAMETER")
  fun handleGooglePlayServicesError(activity: Activity, exception: Exception) = false // always false when bundled
}
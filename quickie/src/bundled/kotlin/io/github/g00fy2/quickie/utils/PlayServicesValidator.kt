package io.github.g00fy2.quickie.utils

import android.app.Activity

internal object PlayServicesValidator {

  @Suppress("UNUSED_PARAMETER")
  fun handleGooglePlayServicesError(activity: Activity, exception: Exception) = false // always false when bundled
}
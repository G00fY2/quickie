package io.github.g00fy2.quickie.utils

import android.app.Activity

internal object MlKitErrorHandler {

  @Suppress("UNUSED_PARAMETER")
  fun isResolvableError(activity: Activity, exception: Exception) = false // always false when bundled
}
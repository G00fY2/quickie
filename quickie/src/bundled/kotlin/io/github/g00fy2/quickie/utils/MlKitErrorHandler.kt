package io.github.g00fy2.quickie.utils

import io.github.g00fy2.quickie.QRScannerActivity

internal object MlKitErrorHandler {

  @Suppress("UNUSED_PARAMETER", "FunctionOnlyReturningConstant")
  fun isResolvableError(activity: QRScannerActivity, exception: Exception) = false // always false when bundled
}
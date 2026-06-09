package io.github.g00fy2.quickie.utils

import io.github.g00fy2.quickie.QRScannerActivity

internal object MlKitErrorHandler {

  @Suppress("detekt:UnusedParameter", "detekt:FunctionOnlyReturningConstant", "SameReturnValue", "unused")
  fun isResolvableError(activity: QRScannerActivity, exception: Exception) = false // always false when bundled
}
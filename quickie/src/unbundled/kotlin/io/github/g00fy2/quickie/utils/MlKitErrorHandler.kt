package io.github.g00fy2.quickie.utils

import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.mlkit.common.MlKitException
import io.github.g00fy2.quickie.QRScannerActivity

internal object MlKitErrorHandler {

  // version 20.12.14 (as suggested https://github.com/firebase/firebase-android-sdk/issues/407#issuecomment-632288258)
  private const val MIN_SERVICES_VERSION = 201214 * 1000
  private const val REQUEST_CODE = 9000

  internal fun isResolvableError(activity: QRScannerActivity, exception: Exception): Boolean {
    if (exception is MlKitException && exception.errorCode == MlKitException.UNAVAILABLE) {
      // check if Google Play services is available and its version is at least MIN_SERVICES_VERSION
      val gmsCode = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(activity, MIN_SERVICES_VERSION)

      if (activity.errorDialog?.isShowing != true && gmsCode != ConnectionResult.SUCCESS &&
        GoogleApiAvailability.getInstance().isUserResolvableError(gmsCode)
      ) {
        activity.errorDialog = GoogleApiAvailability.getInstance().getErrorDialog(activity, gmsCode, REQUEST_CODE)
      }
      return true
    }
    return false
  }
}
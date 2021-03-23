package io.github.g00fy2.quickie.utils

import android.app.Activity
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.mlkit.common.MlKitException

internal object PlayServicesValidator {

  // version 20.12.14 (as suggested https://github.com/firebase/firebase-android-sdk/issues/407#issuecomment-632288258)
  private const val MIN_SERVICES_VERSION = 201214 * 1000
  private const val REQUEST_CODE = 9000

  internal fun handleGooglePlayServicesError(activity: Activity, exception: Exception): Boolean {
    if (exception is MlKitException && exception.errorCode == MlKitException.UNAVAILABLE) {
      // check if Google Play services is available and its version is at least MIN_SERVICES_VERSION
      val resultCode = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(activity, MIN_SERVICES_VERSION)

      if (resultCode != ConnectionResult.SUCCESS &&
        GoogleApiAvailability.getInstance().isUserResolvableError(resultCode)
      ) {
        GoogleApiAvailability.getInstance().getErrorDialog(activity, resultCode, REQUEST_CODE)?.let {
          it.setOnDismissListener { activity.finish() }
          it.show()
          return true
        }
      }
    }
    return false
  }
}
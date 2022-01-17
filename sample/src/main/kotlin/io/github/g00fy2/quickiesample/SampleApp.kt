package io.github.g00fy2.quickiesample

import android.app.Application
import android.os.StrictMode
import androidx.viewbinding.BuildConfig

class SampleApp : Application() {

  override fun onCreate() {
    if (BuildConfig.DEBUG) {
      StrictMode.setThreadPolicy(
        StrictMode.ThreadPolicy.Builder()
          .detectAll()
          .penaltyLog()
          .build()
      )
      StrictMode.setVmPolicy(
        StrictMode.VmPolicy.Builder()
          .detectAll()
          .penaltyLog()
          .build()
      )
    }

    super.onCreate()
  }
}
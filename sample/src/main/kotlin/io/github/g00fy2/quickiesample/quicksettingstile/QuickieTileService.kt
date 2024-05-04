package io.github.g00fy2.quickiesample.quicksettingstile

import android.app.PendingIntent
import android.content.ComponentName
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.service.quicksettings.Tile
import android.service.quicksettings.TileService
import androidx.annotation.RequiresApi
import androidx.core.service.quicksettings.PendingIntentActivityWrapper
import androidx.core.service.quicksettings.TileServiceCompat
import io.github.g00fy2.quickiesample.MainActivity

// optional service to allow launching the sample app from the quick settings
@RequiresApi(Build.VERSION_CODES.N)
class QuickieTileService : TileService() {

  override fun onBind(intent: Intent?): IBinder? {
    requestListeningState(this, ComponentName(this, QuickieTileService::class.java))
    return super.onBind(intent)
  }

  override fun onStartListening() {
    super.onStartListening()
    qsTile?.run {
      state = Tile.STATE_INACTIVE
      updateTile()
    }
  }

  override fun onClick() {
    super.onClick()

    val intent = Intent(this, MainActivity::class.java).apply {
      addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
      putExtra(MainActivity.OPEN_SCANNER, true)
    }

    TileServiceCompat.startActivityAndCollapse(
      this,
      PendingIntentActivityWrapper(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT, true)
    )
  }
}
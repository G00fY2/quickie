package io.github.g00fy2.quickie.config

import io.github.g00fy2.quickie.QRResult

data class ContinuesScanning(val onSuccess :(QRResult.QRSuccess)->Unit, val cooldown: Int)  {
  private var lastSuccess: Long = cooldown.toLong()
  fun updateCooldown() {
    lastSuccess = System.currentTimeMillis()
  }
  fun processResult(result :QRResult.QRSuccess):Boolean {
    if (isInCooldown()) return false
    updateCooldown()
    onSuccess(result)
    return true
  }
  fun isInCooldown(): Boolean {
    return System.currentTimeMillis() - lastSuccess < cooldown
  }
}
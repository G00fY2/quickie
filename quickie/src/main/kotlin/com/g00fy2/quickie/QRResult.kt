package com.g00fy2.quickie

import com.g00fy2.quickie.content.QRContent

sealed class QRResult {

  // MLKit successfully detected a QR code
  data class QRSuccess(val content: QRContent) : QRResult()

  // Activity got cancelled by the user
  object QRUserCanceled : QRResult()

  // Camera permission was not granted
  object QRMissingPermission : QRResult()

  // Error while setting up CameraX or while MLKit analysis
  data class QRError(val exception: Exception) : QRResult()
}
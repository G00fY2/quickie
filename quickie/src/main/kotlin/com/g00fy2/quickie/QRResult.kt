package com.g00fy2.quickie

import com.g00fy2.quickie.content.QRContent

sealed class QRResult {

  data class QRSuccess(val content: QRContent) : QRResult()

  object QRUserCanceled : QRResult()

  object QRMissingPermission : QRResult()

  data class QRError(val exception: Exception) : QRResult()
}
package com.g00fy2.quickie

sealed class QRResult {

  data class QRSuccess(val content: String) : QRResult()

  object QRUserCanceled : QRResult()

  object QRMissingPermission : QRResult()

  data class QRError(val exception: Exception) : QRResult()
}
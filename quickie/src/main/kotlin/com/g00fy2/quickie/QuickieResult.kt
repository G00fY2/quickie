package com.g00fy2.quickie

sealed class QuickieResult {

  data class QuickieSuccess(val content: String) : QuickieResult()

  object QuickieUserCancel : QuickieResult()

  object QuickieMissingPermission : QuickieResult()

  data class QuickieError(val exception: Exception) : QuickieResult()
}
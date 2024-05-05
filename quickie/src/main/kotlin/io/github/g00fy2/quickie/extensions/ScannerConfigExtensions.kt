package io.github.g00fy2.quickie.extensions

import io.github.g00fy2.quickie.config.ParcelableScannerConfig
import io.github.g00fy2.quickie.config.ScannerConfig

internal fun ScannerConfig.toParcelableConfig() =
  ParcelableScannerConfig(
    formats = formats,
    stringRes = stringRes,
    drawableRes = drawableRes,
    hapticFeedback = hapticFeedback,
    showTorchToggle = showTorchToggle,
    horizontalFrameRatio = horizontalFrameRatio,
    useFrontCamera = useFrontCamera,
    showCloseButton = showCloseButton,
    keepScreenOn = keepScreenOn,
  )
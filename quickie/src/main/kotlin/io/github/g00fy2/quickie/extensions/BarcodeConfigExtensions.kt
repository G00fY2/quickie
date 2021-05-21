package io.github.g00fy2.quickie.extensions

import io.github.g00fy2.quickie.config.ParcelableScannerConfig
import io.github.g00fy2.quickie.config.ScannerConfigBuilder.ScannerConfig

internal fun ScannerConfig.toParcelableConfig() = ParcelableScannerConfig(formats, text, icon)
package io.github.g00fy2.quickie.config

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import io.github.g00fy2.quickie.R

/**
 * Builder for ScannerConfig used in ScanBarcode ActivityResultContract.
 */
@Suppress("MemberVisibilityCanBePrivate")
public object ScannerConfigBuilder {

  /**
   * Set a list of interested barcode formats. List must not be empty.
   * Reducing the number of supported formats will make the barcode scanner faster.
   */
  public var barcodeFormats: List<BarcodeFormat> = listOf(BarcodeFormat.FORMAT_ALL_FORMATS)

  /**
   * Set a string resource used for the scanner overlay.
   */
  @StringRes
  public var overlayStringRes: Int = R.string.quickie_scan_qr_code

  /**
   * Set a drawable resource used for the scanner overlay.
   */
  @DrawableRes
  public var overlayDrawableRes: Int = R.drawable.quickie_ic_qrcode

  /**
   * Build the BarcodeConfig required by the ScanBarcode ActivityResultContract.
   */
  public fun build(func: ScannerConfigBuilder.() -> Unit): ScannerConfig = apply { func() }.build()

  /**
   * Build the BarcodeConfig required by the ScanBarcode ActivityResultContract.
   */
  public fun build(): ScannerConfig {
    if (barcodeFormats.isEmpty()) barcodeFormats = listOf(BarcodeFormat.FORMAT_ALL_FORMATS)

    return ScannerConfig(barcodeFormats.map { it.value }.toIntArray(), overlayStringRes, overlayDrawableRes)
  }

  public class ScannerConfig internal constructor(
    internal val formats: IntArray,
    internal val text: Int,
    internal val icon: Int
  )
}
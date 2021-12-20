package io.github.g00fy2.quickie.config

import com.google.mlkit.vision.barcode.common.Barcode

/**
 * Wrapper class to access the ML Kit BarcodeFormat constants.
 */
public enum class BarcodeFormat(internal val value: Int) {
  FORMAT_ALL_FORMATS(Barcode.FORMAT_ALL_FORMATS),
  FORMAT_CODE_128(Barcode.FORMAT_CODE_128),
  FORMAT_CODE_39(Barcode.FORMAT_CODE_39),
  FORMAT_CODE_93(Barcode.FORMAT_CODE_93),
  FORMAT_CODABAR(Barcode.FORMAT_CODABAR),
  FORMAT_DATA_MATRIX(Barcode.FORMAT_DATA_MATRIX),
  FORMAT_EAN_13(Barcode.FORMAT_EAN_13),
  FORMAT_EAN_8(Barcode.FORMAT_EAN_8),
  FORMAT_ITF(Barcode.FORMAT_ITF),
  FORMAT_QR_CODE(Barcode.FORMAT_QR_CODE),
  FORMAT_UPC_A(Barcode.FORMAT_UPC_A),
  FORMAT_UPC_E(Barcode.FORMAT_UPC_E),
  FORMAT_PDF417(Barcode.FORMAT_PDF417),
  FORMAT_AZTEC(Barcode.FORMAT_AZTEC)
}
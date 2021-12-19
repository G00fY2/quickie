package io.github.g00fy2.quickie

import com.google.mlkit.vision.barcode.common.Barcode
import io.github.g00fy2.quickie.config.BarcodeFormat
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class BarcodeFormatsTest {

  @Test
  fun `Ml kit barcode formats are fully mapped`() {
    val mlKitBarcodeFormats: Map<String, Int> = Barcode::class.java.declaredFields
      .filter { it.type == Int::class.java }
      .filter { it.name.startsWith("FORMAT_") }
      .filter { it.name != "FORMAT_UNKNOWN" }
      .map { it.name to it.getInt(null) }
      .toMap()

    val quickieBarcodeFormats: Map<String, Int> = BarcodeFormat.values().map { it.name to it.value }.toMap()

    assertEquals(mlKitBarcodeFormats, quickieBarcodeFormats)
  }
}
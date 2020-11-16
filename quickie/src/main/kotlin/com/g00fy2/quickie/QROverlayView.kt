package com.g00fy2.quickie

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Bitmap.Config.ARGB_8888
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PorterDuff.Mode.CLEAR
import android.graphics.PorterDuffXfermode
import android.graphics.RectF
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import androidx.core.content.ContextCompat
import kotlin.math.min


class QROverlayView @JvmOverloads constructor(
  context: Context,
  attrs: AttributeSet? = null,
  defStyleAttr: Int = 0,
  defStyleRes: Int = 0
) : View(context, attrs, defStyleAttr, defStyleRes) {

  private val cornerColor = ContextCompat.getColor(context, R.color.qr_stroke_color)
  private val highlightedCornerColor = ContextCompat.getColor(context, R.color.qr_highlighted_stroke_color)
  private val backgroundColor = ContextCompat.getColor(context, R.color.qr_background_color)
  private val strokePaint = Paint(Paint.ANTI_ALIAS_FLAG)
  private val transparentPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
    color = ContextCompat.getColor(context, android.R.color.transparent)
    xfermode = PorterDuffXfermode(CLEAR)
  }
  private val backgroundPaint = Paint().apply { color = backgroundColor }
  private val radius = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, OUT_RADIUS, resources.displayMetrics)
  private val innerRadius = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, IN_RADIUS, resources.displayMetrics)
  private var maskBitmap: Bitmap? = null
  private var maskCanvas: Canvas? = null
  private var outerFrame = RectF()
  private var innerFrame = RectF()
  var isHighlighted = false
    set(value) {
      field = value
      invalidate()
    }

  override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
    super.onLayout(changed, left, top, right, bottom)
    if (maskBitmap == null) {
      maskBitmap = Bitmap.createBitmap(width, height, ARGB_8888).apply {
        maskCanvas = Canvas(this)
      }
    }
    calculateFramePos()
  }

  override fun onDraw(canvas: Canvas) {
    strokePaint.color = if (isHighlighted) highlightedCornerColor else cornerColor
    maskCanvas!!.drawColor(backgroundColor)
    maskCanvas!!.drawRoundRect(outerFrame, radius, radius, strokePaint)
    maskCanvas!!.drawRoundRect(innerFrame, innerRadius, innerRadius, transparentPaint)
    canvas.drawBitmap(maskBitmap!!, 0f, 0f, backgroundPaint)
    super.onDraw(canvas)
  }

  private fun calculateFramePos() {
    val centralX = width / 2
    val centralY = height / 2
    val cornerLineLength = min(centralX, centralY) -
        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, FRAME_MARGINS, resources.displayMetrics)
    val strokeWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, STROKE_WIDTH, resources.displayMetrics)
    outerFrame.set(
      centralX - cornerLineLength, centralY - cornerLineLength,
      centralX + cornerLineLength, centralY + cornerLineLength
    )
    innerFrame.set(
      outerFrame.left + strokeWidth, outerFrame.top + strokeWidth,
      outerFrame.right - strokeWidth, outerFrame.bottom - strokeWidth
    )
  }

  companion object {
    private const val STROKE_WIDTH = 4f
    private const val OUT_RADIUS = 16f
    private const val IN_RADIUS = OUT_RADIUS - STROKE_WIDTH
    private const val FRAME_MARGINS = 24f
  }
}
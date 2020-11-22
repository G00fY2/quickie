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
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.annotation.Px
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import kotlin.math.min
import kotlin.math.roundToInt

internal class QROverlayView @JvmOverloads constructor(
  context: Context,
  attrs: AttributeSet? = null,
  defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

  private val strokeColor = ContextCompat.getColor(context, R.color.quickie_stroke_color)
  private val highlightedStrokeColor = ContextCompat.getColor(context, R.color.quickie_highlighted_stroke_color)
  private val backgroundColor = ContextCompat.getColor(context, R.color.quickie_background_color)
  private val strokePaint = Paint(Paint.ANTI_ALIAS_FLAG)
  private val transparentPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
    color = ContextCompat.getColor(context, R.color.quickie_transparent)
    xfermode = PorterDuffXfermode(CLEAR)
  }
  private val backgroundPaint = Paint().apply { color = backgroundColor }
  private val radius = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, OUT_RADIUS, resources.displayMetrics)
  private val innerRadius =
    TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, OUT_RADIUS - STROKE_WIDTH, resources.displayMetrics)
  private val titleTextView: AppCompatTextView
  private var maskBitmap: Bitmap? = null
  private var maskCanvas: Canvas? = null
  private var outerFrame = RectF()
  private var innerFrame = RectF()
  var isHighlighted = false
    set(value) {
      field = value
      invalidate()
    }

  init {
    setWillNotDraw(false)
    LayoutInflater.from(context).inflate(R.layout.quickie_textview, this)
    titleTextView = findViewById(R.id.title_textview)
  }

  override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
    super.onLayout(changed, left, top, right, bottom)
    if (maskBitmap == null) {
      maskBitmap = Bitmap.createBitmap(width, height, ARGB_8888).apply {
        maskCanvas = Canvas(this)
      }
    }
    calculateFrameAndTitlePos()
  }

  override fun onDraw(canvas: Canvas) {
    strokePaint.color = if (isHighlighted) highlightedStrokeColor else strokeColor
    maskCanvas!!.drawColor(backgroundColor)
    maskCanvas!!.drawRoundRect(outerFrame, radius, radius, strokePaint)
    maskCanvas!!.drawRoundRect(innerFrame, innerRadius, innerRadius, transparentPaint)
    canvas.drawBitmap(maskBitmap!!, 0f, 0f, backgroundPaint)
    super.onDraw(canvas)
  }

  private fun calculateFrameAndTitlePos() {
    val centralX = width / 2
    val centralY = height / 2
    val minLength = min(centralX, centralY)
    val strokeLength = minLength - (minLength * FRAME_MARGIN_RATIO)
    val strokeWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, STROKE_WIDTH, resources.displayMetrics)
    outerFrame.set(
      centralX - strokeLength,
      centralY - strokeLength,
      centralX + strokeLength,
      centralY + strokeLength
    )
    innerFrame.set(
      outerFrame.left + strokeWidth,
      outerFrame.top + strokeWidth,
      outerFrame.right - strokeWidth,
      outerFrame.bottom - strokeWidth
    )

    val topInsetsToOuterFrame = (-paddingTop + centralY - strokeLength).roundToInt()
    val titleCenter = (topInsetsToOuterFrame - titleTextView.height) / 2
    titleTextView.updateTopMargin(titleCenter)
    // hide title text if not enough vertical space
    titleTextView.visibility = if (topInsetsToOuterFrame < titleTextView.height) View.INVISIBLE else View.VISIBLE
  }

  private fun View.updateTopMargin(@Px top: Int) {
    val params = layoutParams as MarginLayoutParams
    params.topMargin = top
    layoutParams = params
  }

  companion object {
    private const val STROKE_WIDTH = 4f
    private const val OUT_RADIUS = 16f
    private const val FRAME_MARGIN_RATIO = 1f / 4
  }
}
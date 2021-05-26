package io.github.g00fy2.quickie

import android.content.Context
import android.content.res.Resources.NotFoundException
import android.graphics.Bitmap
import android.graphics.Bitmap.Config.ARGB_8888
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff.Mode.CLEAR
import android.graphics.PorterDuffXfermode
import android.graphics.RectF
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.ColorUtils
import io.github.g00fy2.quickie.databinding.QuickieTextviewBinding
import kotlin.math.min
import kotlin.math.roundToInt

internal class QROverlayView @JvmOverloads constructor(
  context: Context,
  attrs: AttributeSet? = null,
  defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

  private val strokeColor = ContextCompat.getColor(context, R.color.quickie_stroke)
  private val highlightedStrokeColor = getAccentColor()
  private val backgroundColor = ColorUtils.setAlphaComponent(Color.BLACK, BACKGROUND_ALPHA.roundToInt())
  private val alphaPaint = Paint().apply { alpha = BACKGROUND_ALPHA.roundToInt() }
  private val strokePaint = Paint(Paint.ANTI_ALIAS_FLAG)
  private val transparentPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
    color = Color.TRANSPARENT
    xfermode = PorterDuffXfermode(CLEAR)
  }
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
    titleTextView = QuickieTextviewBinding.inflate(LayoutInflater.from(context), this, true).root
  }

  override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
    super.onLayout(changed, left, top, right, bottom)

    if (maskBitmap == null && width > 0 && height > 0) {
      maskBitmap = Bitmap.createBitmap(width, height, ARGB_8888).apply {
        maskCanvas = Canvas(this)
      }
      calculateFrameAndTitlePos()
    }
  }

  override fun onDraw(canvas: Canvas) {
    strokePaint.color = if (isHighlighted) highlightedStrokeColor else strokeColor
    maskCanvas!!.drawColor(backgroundColor)
    maskCanvas!!.drawRoundRect(outerFrame, radius, radius, strokePaint)
    maskCanvas!!.drawRoundRect(innerFrame, innerRadius, innerRadius, transparentPaint)
    canvas.drawBitmap(maskBitmap!!, 0f, 0f, alphaPaint)
    super.onDraw(canvas)
  }

  fun setCustomTextAndIcon(stringRes: Int, drawableRes: Int) {
    if (stringRes != 0) {
      try {
        titleTextView.setText(stringRes)
      } catch (ignore: NotFoundException) {
        // string resource not found
      }
    }
    if (drawableRes != 0) {
      try {
        ResourcesCompat.getDrawable(resources, drawableRes, null)?.limitDrawableSize(ICON_DP_MAX_HEIGHT)?.let {
          titleTextView.setCompoundDrawables(null, it, null, null)
        }
      } catch (ignore: NotFoundException) {
        // drawable resource not found
      }
    }
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

  private fun getAccentColor(): Int {
    return TypedValue().let {
      if (context.theme.resolveAttribute(android.R.attr.colorAccent, it, true)) it.data else Color.WHITE
    }
  }

  private fun View.updateTopMargin(topPx: Int) {
    val params = layoutParams as MarginLayoutParams
    params.topMargin = topPx
    layoutParams = params
  }

  private fun Drawable.limitDrawableSize(maxDpHeight: Int): Drawable {
    val scale = (maxDpHeight * resources.displayMetrics.density) / minimumHeight
    if (scale < 1) {
      setBounds(0, 0, (minimumWidth * scale).roundToInt(), (minimumHeight * scale).roundToInt())
    } else {
      setBounds(0, 0, minimumWidth, minimumHeight)
    }
    return this
  }

  companion object {
    private const val BACKGROUND_ALPHA = 0.77 * 255
    private const val STROKE_WIDTH = 4f
    private const val OUT_RADIUS = 16f
    private const val FRAME_MARGIN_RATIO = 1f / 4
    private const val ICON_DP_MAX_HEIGHT = 56
  }
}
package io.github.g00fy2.quickie

import android.content.Context
import android.content.res.ColorStateList
import android.content.res.Resources.NotFoundException
import android.graphics.Bitmap
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
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.ColorUtils
import androidx.core.graphics.drawable.DrawableCompat
import io.github.g00fy2.quickie.databinding.QuickieOverlayViewBinding
import kotlin.math.min
import kotlin.math.roundToInt

@Suppress("TooManyFunctions")
internal class QROverlayView @JvmOverloads constructor(
  context: Context,
  attrs: AttributeSet? = null,
  defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

  private val binding = QuickieOverlayViewBinding.inflate(LayoutInflater.from(context), this)
  private val grayColor = ContextCompat.getColor(context, R.color.quickie_gray)
  private val accentColor = getAccentColor()
  private val backgroundColor = ColorUtils.setAlphaComponent(Color.BLACK, BACKGROUND_ALPHA.roundToInt())
  private val alphaPaint = Paint().apply { alpha = BACKGROUND_ALPHA.roundToInt() }
  private val strokePaint = Paint(Paint.ANTI_ALIAS_FLAG)
  private val loadingBackgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = backgroundColor }
  private val transparentPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
    color = Color.TRANSPARENT
    xfermode = PorterDuffXfermode(CLEAR)
  }
  private val outerRadius = OUT_RADIUS.toPx()
  private val innerRadius = (OUT_RADIUS - STROKE_WIDTH).toPx()
  private val outerFrame = RectF()
  private val innerFrame = RectF()
  private var maskBitmap: Bitmap? = null
  private var maskCanvas: Canvas? = null
  private var horizontalFrameRatio = 1f
  var isHighlighted = false
    set(value) {
      if (field != value) {
        field = value
        invalidate()
      }
    }
  var isLoading = false
    set(value) {
      if (field != value) {
        field = value
        binding.progressView.visibility = if (value) View.VISIBLE else View.GONE
      }
    }

  init {
    setWillNotDraw(false)
  }

  override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
    super.onLayout(changed, left, top, right, bottom)

    if (maskBitmap == null && width > 0 && height > 0) {
      maskBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888).apply { maskCanvas = Canvas(this) }
      calculateFrameAndTitlePos()
    }
  }

  @Suppress("UnsafeCallOnNullableType")
  override fun onDraw(canvas: Canvas) {
    strokePaint.color = if (isHighlighted) accentColor else grayColor
    maskCanvas!!.drawColor(backgroundColor)
    maskCanvas!!.drawRoundRect(outerFrame, outerRadius, outerRadius, strokePaint)
    maskCanvas!!.drawRoundRect(innerFrame, innerRadius, innerRadius, transparentPaint)
    if (isLoading) maskCanvas!!.drawRoundRect(innerFrame, innerRadius, innerRadius, loadingBackgroundPaint)
    canvas.drawBitmap(maskBitmap!!, 0f, 0f, alphaPaint)
    super.onDraw(canvas)
  }

  fun setCustomText(stringRes: Int) {
    if (stringRes != 0) {
      try {
        binding.titleTextView.setText(stringRes)
      } catch (ignore: NotFoundException) {
        // string resource not found
      }
    }
  }

  fun setCustomIcon(drawableRes: Int?) {
    if (drawableRes == null) {
      binding.titleTextView.setCompoundDrawables(null, null, null, null)
    } else if (drawableRes != 0) {
      try {
        ResourcesCompat.getDrawable(resources, drawableRes, null)?.limitDrawableSize()?.let {
          binding.titleTextView.setCompoundDrawables(null, it, null, null)
        }
      } catch (ignore: NotFoundException) {
        // drawable resource not found
      }
    }
  }

  fun setHorizontalFrameRatio(ratio: Float) {
    if (ratio > 1f) {
      horizontalFrameRatio = ratio
      calculateFrameAndTitlePos()
    }
  }

  fun setCloseVisibilityAndOnClick(visible: Boolean, action: () -> Unit = {}) {
    binding.closeImageView.visibility = if (visible) View.VISIBLE else View.GONE
    binding.closeImageView.setOnClickListener { action() }
    if (visible) binding.closeImageView.setTintAndStateAwareBackground()
  }

  fun setTorchVisibilityAndOnClick(visible: Boolean, action: (Boolean) -> Unit = {}) {
    binding.torchImageView.visibility = if (visible) View.VISIBLE else View.GONE
    binding.torchImageView.setOnClickListener { action(!it.isSelected) }
    if (visible) binding.torchImageView.setTintAndStateAwareBackground()
  }

  fun setTorchState(on: Boolean) {
    binding.torchImageView.isSelected = on
  }

  private fun calculateFrameAndTitlePos() {
    val centralX = width / 2
    val centralY = height / 2
    val minLength = min(centralX, centralY)
    val marginRatio = if (horizontalFrameRatio > 1f) {
      FRAME_MARGIN_RATIO * ((1f / horizontalFrameRatio) * 1.5f)
    } else {
      FRAME_MARGIN_RATIO
    }
    val strokeLength = minLength - (minLength * marginRatio)
    val strokeWidth = STROKE_WIDTH.toPx()
    outerFrame.set(
      centralX - strokeLength,
      centralY - strokeLength / horizontalFrameRatio,
      centralX + strokeLength,
      centralY + strokeLength / horizontalFrameRatio
    )
    innerFrame.set(
      outerFrame.left + strokeWidth,
      outerFrame.top + strokeWidth,
      outerFrame.right - strokeWidth,
      outerFrame.bottom - strokeWidth
    )

    val topInsetsToOuterFrame = (-paddingTop + centralY - strokeLength).roundToInt()
    val titleCenter = (topInsetsToOuterFrame - binding.titleTextView.height) / 2
    binding.titleTextView.updateTopMargin(titleCenter)
    // hide title text if not enough vertical space
    binding.titleTextView.visibility =
      if (topInsetsToOuterFrame < binding.titleTextView.height) View.INVISIBLE else View.VISIBLE
  }

  private fun getAccentColor(): Int {
    return TypedValue().let {
      if (context.theme.resolveAttribute(android.R.attr.colorAccent, it, true)) {
        it.data
      } else {
        ContextCompat.getColor(context, R.color.quickie_accent_fallback)
      }
    }
  }

  private fun View.updateTopMargin(topPx: Int) {
    val params = layoutParams as MarginLayoutParams
    params.topMargin = topPx
    layoutParams = params
  }

  private fun Drawable.limitDrawableSize(): Drawable {
    val heightLimit = ICON_MAX_HEIGHT.toPx()
    val scale = heightLimit / minimumHeight
    if (scale < 1) {
      setBounds(0, 0, (minimumWidth * scale).roundToInt(), (minimumHeight * scale).roundToInt())
    } else {
      setBounds(0, 0, minimumWidth, minimumHeight)
    }
    return this
  }

  private fun View.setTintAndStateAwareBackground() {
    background?.let { drawable ->
      val wrappedDrawable = DrawableCompat.wrap(drawable)

      val states = arrayOf(
        intArrayOf(android.R.attr.state_pressed, android.R.attr.state_selected),
        intArrayOf(android.R.attr.state_pressed, -android.R.attr.state_selected),
        intArrayOf(-android.R.attr.state_pressed, android.R.attr.state_selected),
        intArrayOf()
      )
      val stateColors = intArrayOf(grayColor, accentColor, accentColor, grayColor)
      val colorStateList = ColorStateList(states, stateColors).withAlpha(BUTTON_BACKGROUND_ALPHA.roundToInt())

      DrawableCompat.setTintList(wrappedDrawable, colorStateList)
      background = wrappedDrawable
    }
  }

  private fun Float.toPx() =
    TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this, resources.displayMetrics)

  companion object {
    private const val BACKGROUND_ALPHA = 0.77 * 255
    private const val BUTTON_BACKGROUND_ALPHA = 0.6 * 255
    private const val STROKE_WIDTH = 4f
    private const val OUT_RADIUS = 16f
    private const val FRAME_MARGIN_RATIO = 1f / 4
    private const val ICON_MAX_HEIGHT = 56f
  }
}
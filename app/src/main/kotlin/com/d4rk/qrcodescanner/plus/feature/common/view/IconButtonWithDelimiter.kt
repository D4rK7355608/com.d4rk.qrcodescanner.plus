package com.d4rk.qrcodescanner.plus.feature.common.view
import android.content.Context
import android.content.res.TypedArray
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import com.d4rk.qrcodescanner.plus.R
import kotlinx.android.synthetic.main.layout_icon_button_with_delimiter.view.layout_image
import kotlinx.android.synthetic.main.layout_icon_button_with_delimiter.view.text_view
import kotlinx.android.synthetic.main.layout_icon_button_with_delimiter.view.image_view_arrow
import kotlinx.android.synthetic.main.layout_icon_button_with_delimiter.view.delimiter
import kotlinx.android.synthetic.main.layout_icon_button_with_delimiter.view.image_view_schema
class IconButtonWithDelimiter : FrameLayout {
    private val view: View = LayoutInflater.from(context).inflate(R.layout.layout_icon_button_with_delimiter, this, true)
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, -1)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        context.obtainStyledAttributes(attrs, R.styleable.IconButtonWithDelimiter).apply {
            showIcon(this)
            showIconBackgroundColor(this)
            showText(this)
            showArrow(this)
            showDelimiter(this)
            recycle()
        }
    }
    private fun showIcon(attributes: TypedArray) {
        val iconResId = attributes.getResourceId(R.styleable.IconButtonWithDelimiter_icon, -1)
        val icon = AppCompatResources.getDrawable(context, iconResId)
        view.image_view_schema.setImageDrawable(icon)
    }
    private fun showIconBackgroundColor(attributes: TypedArray) {
        val color = attributes.getColor(R.styleable.IconButtonWithDelimiter_iconBackground, ContextCompat.getColor(context, R.color.colorGoogleGreen))
        (view.layout_image.background.mutate() as GradientDrawable).setColor(color)
    }
    private fun showText(attributes: TypedArray) {
        view.text_view.text = attributes.getString(R.styleable.IconButtonWithDelimiter_text).orEmpty()
    }
    private fun showArrow(attributes: TypedArray) {
        view.image_view_arrow.isVisible = attributes.getBoolean(R.styleable.IconButtonWithDelimiter_isArrowVisible, false)
    }
    private fun showDelimiter(attributes: TypedArray) {
        view.delimiter.isInvisible = attributes.getBoolean(R.styleable.IconButtonWithDelimiter_isDelimiterVisible, true).not()
    }
    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)
        view.image_view_schema.isEnabled = enabled
        view.text_view.isEnabled = enabled
    }
}
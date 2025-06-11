package com.d4rk.qrcodescanner.plus.ui.view
import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import com.d4rk.qrcodescanner.plus.R
import com.d4rk.qrcodescanner.plus.databinding.LayoutDateTimePickerButtonBinding
import com.d4rk.qrcodescanner.plus.extension.formatOrNull
import com.github.florent37.singledateandtimepicker.dialog.SingleDateAndTimePickerDialog
import java.text.SimpleDateFormat
import java.util.Locale
class DateTimePickerButton : FrameLayout {
    private lateinit var binding : LayoutDateTimePickerButtonBinding
    private val dateFormatter = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.ENGLISH)
    private val view: View = LayoutInflater.from(context).inflate(R.layout.layout_date_time_picker_button, this, true)
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, -1)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        context.obtainStyledAttributes(attrs, R.styleable.DateTimePickerButton).apply {
            showHint(this)
            recycle()
        }
        view.setOnClickListener {
            showDateTimePickerDialog()
        }
        showDateTime()
    }
    var dateTime: Long = System.currentTimeMillis()
        set(value) {
            field = value
            showDateTime()
        }
    private fun showHint(attributes: TypedArray) {
        binding.textViewHint.text = attributes.getString(R.styleable.DateTimePickerButton_hint).orEmpty()
    }
    private fun showDateTimePickerDialog() {
        SingleDateAndTimePickerDialog.Builder(context)
            .title(binding.textViewHint.text.toString())
            .listener { newDateTime ->
                dateTime = newDateTime.time
                showDateTime()
            }
            .display()
    }
    private fun showDateTime() {
        binding.textViewDateTime.text = dateFormatter.formatOrNull(dateTime).orEmpty()
    }
}
package com.d4rk.qrcodescanner.plus.feature.tabs.create.qr
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.d4rk.qrcodescanner.plus.R
import com.d4rk.qrcodescanner.plus.extension.textString
import com.d4rk.qrcodescanner.plus.feature.tabs.create.BaseCreateBarcodeFragment
import com.d4rk.qrcodescanner.plus.model.schema.Schema
import com.d4rk.qrcodescanner.plus.model.schema.VEvent
import kotlinx.android.synthetic.main.fragment_create_qr_code_vevent.edit_text_title
import kotlinx.android.synthetic.main.fragment_create_qr_code_vevent.edit_text_summary
import kotlinx.android.synthetic.main.fragment_create_qr_code_vevent.edit_text_organizer
import kotlinx.android.synthetic.main.fragment_create_qr_code_vevent.button_date_time_start
import kotlinx.android.synthetic.main.fragment_create_qr_code_vevent.button_date_time_end
class CreateQrCodeEventFragment : BaseCreateBarcodeFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_create_qr_code_vevent, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        edit_text_title.requestFocus()
        parentActivity.isCreateBarcodeButtonEnabled = true
    }
    override fun getBarcodeSchema(): Schema {
        return VEvent(
            uid = edit_text_title.textString,
            organizer = edit_text_organizer.textString,
            summary = edit_text_summary.textString,
            startDate = button_date_time_start.dateTime,
            endDate = button_date_time_end.dateTime
        )
    }
}
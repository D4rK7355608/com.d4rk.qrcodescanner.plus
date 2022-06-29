package com.d4rk.qrcodescanner.plus.feature.tabs.create.qr
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.d4rk.qrcodescanner.plus.R
import com.d4rk.qrcodescanner.plus.extension.textString
import com.d4rk.qrcodescanner.plus.feature.tabs.create.BaseCreateBarcodeFragment
import com.d4rk.qrcodescanner.plus.model.Contact
import com.d4rk.qrcodescanner.plus.model.schema.MeCard
import com.d4rk.qrcodescanner.plus.model.schema.Schema
import kotlinx.android.synthetic.main.fragment_create_qr_code_mecard.edit_text_first_name
import kotlinx.android.synthetic.main.fragment_create_qr_code_mecard.edit_text_email
import kotlinx.android.synthetic.main.fragment_create_qr_code_mecard.edit_text_last_name
import kotlinx.android.synthetic.main.fragment_create_qr_code_mecard.edit_text_phone
class CreateQrCodeMeCardFragment : BaseCreateBarcodeFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_create_qr_code_mecard, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        edit_text_first_name.requestFocus()
        parentActivity.isCreateBarcodeButtonEnabled = true
    }
    override fun getBarcodeSchema(): Schema {
        return MeCard(
            firstName = edit_text_first_name.textString,
            lastName = edit_text_last_name.textString,
            email = edit_text_email.textString,
            phone = edit_text_phone.textString
        )
    }
    override fun showContact(contact: Contact) {
        edit_text_first_name.setText(contact.firstName)
        edit_text_last_name.setText(contact.lastName)
        edit_text_email.setText(contact.email)
        edit_text_phone.setText(contact.phone)
    }
}
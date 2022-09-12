package com.d4rk.qrcodescanner.plus.feature.common.dialog
import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.d4rk.qrcodescanner.plus.R
import com.d4rk.qrcodescanner.plus.databinding.DialogEditBarcodeNameBinding
class EditBarcodeNameDialogFragment : DialogFragment() {
    private lateinit var _binding: DialogEditBarcodeNameBinding
    private val binding get() = _binding
    interface Listener {
        fun onNameConfirmed(name: String)
    }
    companion object {
        private const val NAME_KEY = "NAME_KEY"
        fun newInstance(name: String?): EditBarcodeNameDialogFragment {
            return EditBarcodeNameDialogFragment().apply {
                arguments = Bundle().apply {
                    putString(NAME_KEY, name)
                }
            }
        }
    }
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val listener = requireActivity() as? Listener
        val name = arguments?.getString(NAME_KEY).orEmpty()
        val view = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_edit_barcode_name, null, false)
        val dialog = AlertDialog.Builder(requireActivity()).setTitle(R.string.dialog_edit_barcode_name_title).setView(view).setPositiveButton(android.R.string.ok) { _, _ ->
                val newName = binding.editTextBarcodeName.text.toString()
                listener?.onNameConfirmed(newName)
            }
            .setNegativeButton(android.R.string.ok, null).create()
        dialog.setOnShowListener {
            initNameEditText(binding.editTextBarcodeName, name)
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(requireContext(), R.color.colorAccent))
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(requireContext(), R.color.colorGoogleRed))
        }
        return dialog
    }
    private fun initNameEditText(editText: EditText, name: String) {
        editText.apply {
            setText(name)
            setSelection(name.length)
            requestFocus()
        }
        val manager = requireContext().getSystemService(Activity.INPUT_METHOD_SERVICE) as? InputMethodManager
        @Suppress("DEPRECATION")
        manager?.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0)
    }
}
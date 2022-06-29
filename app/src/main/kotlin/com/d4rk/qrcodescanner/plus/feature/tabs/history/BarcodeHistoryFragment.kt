package com.d4rk.qrcodescanner.plus.feature.tabs.history
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.d4rk.qrcodescanner.plus.R
import com.d4rk.qrcodescanner.plus.databinding.FragmentBarcodeHistoryBinding
import com.d4rk.qrcodescanner.plus.di.barcodeDatabase
import com.d4rk.qrcodescanner.plus.extension.applySystemWindowInsets
import com.d4rk.qrcodescanner.plus.extension.showError
import com.d4rk.qrcodescanner.plus.feature.common.dialog.DeleteConfirmationDialogFragment
import com.d4rk.qrcodescanner.plus.feature.tabs.history.export.ExportHistoryActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_barcode_history.app_bar_layout
import kotlinx.android.synthetic.main.fragment_barcode_history.view_pager
import kotlinx.android.synthetic.main.fragment_barcode_history.toolbar
import kotlinx.android.synthetic.main.fragment_barcode_history.tab_layout
class BarcodeHistoryFragment : Fragment(), DeleteConfirmationDialogFragment.Listener {
    private lateinit var _binding: FragmentBarcodeHistoryBinding
    private val binding get() = _binding
    private val disposable = CompositeDisposable()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding =  FragmentBarcodeHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        supportEdgeToEdge()
        initTabs()
        handleMenuClicked()
    }
    override fun onDeleteConfirmed() {
        clearHistory()
    }
    override fun onDestroyView() {
        super.onDestroyView()
        disposable.clear()
    }
    private fun supportEdgeToEdge() {
        app_bar_layout.applySystemWindowInsets(applyTop = true)
    }
    private fun initTabs() {
        view_pager.adapter = BarcodeHistoryViewPagerAdapter(requireContext(), childFragmentManager)
        tab_layout.setupWithViewPager(view_pager)
    }
    private fun handleMenuClicked() {
        toolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.item_export_history -> navigateToExportHistoryScreen()
                R.id.item_clear_history -> showDeleteHistoryConfirmationDialog()
            }
            return@setOnMenuItemClickListener true
        }
    }
    private fun navigateToExportHistoryScreen() {
        ExportHistoryActivity.start(requireActivity())
    }
    private fun showDeleteHistoryConfirmationDialog() {
        val dialog = DeleteConfirmationDialogFragment.newInstance(R.string.dialog_delete_clear_history_message)
        dialog.show(childFragmentManager, "")
    }
    private fun clearHistory() {
        barcodeDatabase.deleteAll().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({
            }, ::showError).addTo(disposable)
    }
}
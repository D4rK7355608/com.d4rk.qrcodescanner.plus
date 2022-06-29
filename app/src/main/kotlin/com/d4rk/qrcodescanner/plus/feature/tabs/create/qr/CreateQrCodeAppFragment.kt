package com.d4rk.qrcodescanner.plus.feature.tabs.create.qr
import android.content.Intent
import android.content.pm.ResolveInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.d4rk.qrcodescanner.plus.R
import com.d4rk.qrcodescanner.plus.extension.showError
import com.d4rk.qrcodescanner.plus.extension.unsafeLazy
import com.d4rk.qrcodescanner.plus.feature.tabs.create.BaseCreateBarcodeFragment
import com.d4rk.qrcodescanner.plus.model.schema.App
import com.d4rk.qrcodescanner.plus.model.schema.Schema
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_create_qr_code_app.recycler_view_apps
import kotlinx.android.synthetic.main.fragment_create_qr_code_app.progress_bar_loading
class CreateQrCodeAppFragment : BaseCreateBarcodeFragment() {
    private val disposable = CompositeDisposable()
    private val appAdapter by unsafeLazy { AppAdapter(parentActivity) }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_create_qr_code_app, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        loadApps()
    }
    override fun getBarcodeSchema(): Schema {
        return App.fromPackage("")
    }
    override fun onDestroyView() {
        super.onDestroyView()
        disposable.clear()
    }
    private fun initRecyclerView() {
        recycler_view_apps.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = appAdapter
        }
    }
    private fun loadApps() {
        showLoading(true)
        Single.just(getApps())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { apps ->
                    showLoading(false)
                    showApps(apps)
                },
                { error ->
                    showLoading(false)
                    showError(error)
                }
            )
            .addTo(disposable)
    }
    private fun getApps(): List<ResolveInfo> {
        val mainIntent = Intent(Intent.ACTION_MAIN).apply {
            addCategory(Intent.CATEGORY_LAUNCHER)
        }
        return requireContext().packageManager
            .queryIntentActivities(mainIntent, 0)
            .filter { it.activityInfo?.packageName != null }
    }
    private fun showLoading(isLoading: Boolean) {
        progress_bar_loading.isVisible = isLoading
        recycler_view_apps.isVisible = isLoading.not()
    }
    private fun showApps(apps: List<ResolveInfo>) {
        appAdapter.apps = apps
    }
}
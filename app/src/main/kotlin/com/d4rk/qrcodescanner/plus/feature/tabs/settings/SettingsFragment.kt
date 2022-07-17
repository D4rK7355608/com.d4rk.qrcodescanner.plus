package com.d4rk.qrcodescanner.plus.feature.tabs.settings
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.d4rk.qrcodescanner.plus.R
import com.d4rk.qrcodescanner.plus.databinding.FragmentSettingsBinding
import com.d4rk.qrcodescanner.plus.di.barcodeDatabase
import com.d4rk.qrcodescanner.plus.di.settings
import com.d4rk.qrcodescanner.plus.extension.applySystemWindowInsets
import com.d4rk.qrcodescanner.plus.extension.showError
import com.d4rk.qrcodescanner.plus.feature.common.dialog.DeleteConfirmationDialogFragment
import com.d4rk.qrcodescanner.plus.feature.tabs.settings.camera.ChooseCameraActivity
import com.d4rk.qrcodescanner.plus.feature.tabs.settings.formats.SupportedFormatsActivity
import com.d4rk.qrcodescanner.plus.feature.tabs.settings.about.AboutActivity
import com.d4rk.qrcodescanner.plus.feature.tabs.settings.feedback.FeedbackActivity
import com.d4rk.qrcodescanner.plus.feature.tabs.settings.language.LanguageActivity
import com.d4rk.qrcodescanner.plus.feature.tabs.settings.permissions.AllPermissionsActivity
import com.d4rk.qrcodescanner.plus.feature.tabs.settings.search.ChooseSearchEngineActivity
import com.d4rk.qrcodescanner.plus.feature.tabs.settings.theme.ChooseThemeActivity
import com.google.android.material.textview.MaterialTextView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
class SettingsFragment : Fragment(), DeleteConfirmationDialogFragment.Listener {
    private lateinit var _binding: FragmentSettingsBinding
    private val binding get() = _binding
    private val disposable = CompositeDisposable()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        binding.buttonCheckUpdates.setOnClickListener {
            val uri = Uri.parse("market://details?id=" + requireContext().packageName)
            val intent = Intent(Intent.ACTION_VIEW, uri).apply {
                flags = Intent.FLAG_ACTIVITY_NO_HISTORY or Intent.FLAG_ACTIVITY_NEW_DOCUMENT or Intent.FLAG_ACTIVITY_MULTIPLE_TASK
            }
            if (intent.resolveActivity(requireContext().packageManager) != null) {
                startActivity(intent)
            }
        }
        binding.buttonMore.setOnClickListener {
            val intent = Intent(activity, AboutActivity::class.java)
            startActivity(intent)
        }
        binding.buttonLicense.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.gnu.org/licenses/gpl-3.0"))
            startActivity(intent)
        }
        binding.buttonFeedback.setOnClickListener {
            val intent = Intent(activity, FeedbackActivity::class.java)
            startActivity(intent)
        }
        binding.buttonLanguage.setOnClickListener {
            val intent = Intent(activity, LanguageActivity::class.java)
            startActivity(intent)
        }
        binding.buttonPrivacyPolicy.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://sites.google.com/view/d4rk7355608/more/apps/qr-bar-code-scanner/privacy-policy"))
            startActivity(intent)
        }
        binding.buttonTermsOfService.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://sites.google.com/view/d4rk7355608/more/apps/qr-bar-code-scanner/terms-of-service"))
            startActivity(intent)
        }
        binding.buttonCodeOfConduct.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://sites.google.com/view/d4rk7355608/more/code-of-conduct"))
            startActivity(intent)
        }
        binding.buttonMoreApps.setOnClickListener {
            val alertDialog: AlertDialog.Builder = AlertDialog.Builder(requireContext())
            alertDialog.setTitle(R.string.more_apps)
            val view: View = layoutInflater.inflate(R.layout.fragment_dialog, null)
            val musicSleepTimerString: MaterialTextView = view.findViewById(R.id.musicSleepTimerString)
            val englishWithLidiaString: MaterialTextView = view.findViewById(R.id.englishWithLidiaString)
            alertDialog.setView(view)
            alertDialog.create()
            view.findViewById<View?>(R.id.musicSleepTimer)?.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.d4rk.musicsleeptimer.plus"))
                startActivity(intent)
            }
            musicSleepTimerString.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.d4rk.musicsleeptimer.plus"))
                startActivity(intent)
            }
            view.findViewById<View?>(R.id.englishWithLidia)?.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.d4rk.englishwithlidia.plus"))
                startActivity(intent)
            }
            englishWithLidiaString.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.d4rk.englishwithlidia.plus"))
                startActivity(intent)
            }
            alertDialog.setNegativeButton(R.string.cool, null)
            alertDialog.show()
        }
        binding.buttonPermissions.setOnClickListener {
            AllPermissionsActivity.start(requireActivity())
        }
        binding.buttonChooseCamera.setOnClickListener{
            ChooseCameraActivity.start(requireActivity())
        }
        binding.buttonChooseTheme.setOnClickListener {
            ChooseThemeActivity.start(requireActivity())
        }
        binding.buttonChangelog.setOnClickListener {
            val alertDialog = AlertDialog.Builder(requireActivity())
            alertDialog.setTitle(R.string.fragment_settings_changelog)
            alertDialog.setMessage(R.string.changelog)
            alertDialog.setPositiveButton("Cool!") { dialog: DialogInterface, _: Int -> dialog.dismiss() }
            alertDialog.show()
        }
        binding.buttonSelectSupportedFormats.setOnClickListener {
            SupportedFormatsActivity.start(requireActivity())
        }
        binding.buttonClearHistory.setOnClickListener {
            showDeleteHistoryConfirmationDialog()
        }
        binding.buttonChooseSearchEngine.setOnClickListener {
            ChooseSearchEngineActivity.start(requireContext())
        }
        binding.buttonFlashlight.setCheckedChangedListener {
            settings.flash = it
        }
        binding.buttonDoNotSaveDuplicates.setCheckedChangedListener{
            settings.doNotSaveDuplicates = it
        }
        binding.buttonInverseBarcodeColorsInDarkTheme.setCheckedChangedListener{
            settings.areBarcodeColorsInversed = it
        }
        binding.buttonOpenLinksAutomatically.setCheckedChangedListener{
            settings.openLinksAutomatically = it
        }
        binding.buttonCopyToClipboard.setCheckedChangedListener{
            settings.copyToClipboard = it
        }
        binding.buttonSimpleAutoFocus.setCheckedChangedListener{
            settings.simpleAutoFocus = it
        }
        binding.buttonVibrate.setCheckedChangedListener{
            settings.vibrate = it
        }
        binding.buttonContinuousScanning.setCheckedChangedListener{
            settings.continuousScanning = it
        }
        binding.buttonConfirmScansManually.setCheckedChangedListener{
            settings.confirmScansManually = it
        }
        binding.buttonSaveScannedBarcodes.setCheckedChangedListener{
            settings.saveScannedBarcodesToHistory = it
        }
        binding.buttonSaveCreatedBarcodes.setCheckedChangedListener{
            settings.saveCreatedBarcodesToHistory = it
        }
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        supportEdgeToEdge()
    }
    override fun onResume() {
        super.onResume()
        showSettings()
    }
    override fun onDeleteConfirmed() {
        clearHistory()
    }
    override fun onDestroyView() {
        super.onDestroyView()
        disposable.clear()
    }
    fun supportEdgeToEdge() {
        binding.appBarLayout.applySystemWindowInsets(applyTop = true)
    }
    private fun clearHistory() {
        binding.buttonClearHistory.isEnabled = false
        barcodeDatabase.deleteAll()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    binding.buttonClearHistory.isEnabled = true
                },
                { error ->
                    binding.buttonClearHistory.isEnabled = true
                    showError(error)
                }
            )
            .addTo(disposable)
    }
    private fun showSettings() {
        settings.apply {
            binding.buttonInverseBarcodeColorsInDarkTheme.isChecked = areBarcodeColorsInversed
            binding.buttonOpenLinksAutomatically.isChecked = openLinksAutomatically
            binding.buttonCopyToClipboard.isChecked = copyToClipboard
            binding.buttonSimpleAutoFocus.isChecked = simpleAutoFocus
            binding.buttonFlashlight.isChecked = flash
            binding.buttonVibrate.isChecked = vibrate
            binding.buttonContinuousScanning.isChecked = continuousScanning
            binding.buttonConfirmScansManually.isChecked = confirmScansManually
            binding.buttonSaveScannedBarcodes.isChecked = saveScannedBarcodesToHistory
            binding.buttonSaveCreatedBarcodes.isChecked = saveCreatedBarcodesToHistory
            binding.buttonDoNotSaveDuplicates.isChecked = doNotSaveDuplicates
        }
    }
    private fun showDeleteHistoryConfirmationDialog() {
        val dialog = DeleteConfirmationDialogFragment.newInstance(R.string.dialog_delete_clear_history_message)
        dialog.show(childFragmentManager, "")
    }
}
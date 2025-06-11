package com.d4rk.qrcodescanner.plus.ui.settings
import android.content.SharedPreferences
import android.content.Intent
import android.content.ClipboardManager
import android.content.ClipData
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import androidx.preference.SwitchPreferenceCompat
import com.d4rk.qrcodescanner.plus.BuildConfig
import com.d4rk.qrcodescanner.plus.R
import com.d4rk.qrcodescanner.plus.databinding.ActivitySettingsBinding
import com.d4rk.qrcodescanner.plus.di.settings
import com.d4rk.qrcodescanner.plus.ui.dialogs.DeleteConfirmationDialogFragment
import com.d4rk.qrcodescanner.plus.ui.dialogs.RequireRestartDialog
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar

class SettingsActivity : AppCompatActivity(), SharedPreferences.OnSharedPreferenceChangeListener {
    private lateinit var binding: ActivitySettingsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportFragmentManager.beginTransaction().replace(R.id.settings, SettingsFragment()).commit()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this)
    }
    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, rootKey: String?) {
        val themeValues = resources.getStringArray(R.array.preference_theme_values)
        when (rootKey) {
            getString(R.string.key_theme) -> sharedPreferences?.let { pref ->
                when (pref.getString(getString(R.string.key_theme), themeValues[0])) {
                    themeValues[0] -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                    themeValues[1] -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    themeValues[2] -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    themeValues[3] -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY)
                }
            }
        }
        val languageCode = sharedPreferences?.getString(getString(R.string.key_language), getString(R.string.default_value_language))
        AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(languageCode))
    }
    class SettingsFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.preferences_settings, rootKey)
            val labelVisibilityMode = findPreference<ListPreference>(getString(R.string.key_bottom_navigation_bar_labels))
            labelVisibilityMode?.setOnPreferenceChangeListener { _, _ ->
                val restartDialog = RequireRestartDialog()
                restartDialog.show(childFragmentManager, RequireRestartDialog::class.java.name)
                true
            }
            val flashlightPreference = findPreference<SwitchPreferenceCompat>(getString(R.string.key_flashlight))
            flashlightPreference?.setOnPreferenceChangeListener { _, newValue ->
                settings.flash = newValue as Boolean
                true
            }
            val defaultTab = findPreference<ListPreference>(getString(R.string.key_default_tab))
            defaultTab?.setOnPreferenceChangeListener { _, _ ->
                val restartDialog = RequireRestartDialog()
                restartDialog.show(childFragmentManager, RequireRestartDialog::class.java.name)
                true
            }
            val inverseBarcodeColorsInDarkThemePreference = findPreference<SwitchPreferenceCompat>(getString(R.string.key_invert_bar_code_colors_in_dark_theme))
            inverseBarcodeColorsInDarkThemePreference?.setOnPreferenceChangeListener { _, newValue ->
                settings.areBarcodeColorsInversed = newValue as Boolean
                true
            }
            val doNotSaveDuplicatesPreference = findPreference<SwitchPreferenceCompat>(getString(R.string.key_do_not_save_duplicates))
            doNotSaveDuplicatesPreference?.setOnPreferenceChangeListener { _, newValue ->
                settings.doNotSaveDuplicates = newValue as Boolean
                true
            }
            val copyToClipboardPreference = findPreference<SwitchPreferenceCompat>(getString(R.string.key_copy_to_clipboard))
            copyToClipboardPreference?.setOnPreferenceChangeListener { _, newValue ->
                settings.copyToClipboard = newValue as Boolean
                true
            }
            val simpleAutoFocusPreference = findPreference<SwitchPreferenceCompat>(getString(R.string.key_simple_auto_focus))
            simpleAutoFocusPreference?.setOnPreferenceChangeListener { _, newValue ->
                settings.simpleAutoFocus = newValue as Boolean
                true
            }
            val vibratePreference = findPreference<SwitchPreferenceCompat>(getString(R.string.key_vibrate))
            vibratePreference?.setOnPreferenceChangeListener { _, newValue ->
                settings.vibrate = newValue as Boolean
                true
            }
            val continuousScanningPreference = findPreference<SwitchPreferenceCompat>(getString(R.string.key_continuous_scanning))
            continuousScanningPreference?.setOnPreferenceChangeListener { _, newValue ->
                settings.continuousScanning = newValue as Boolean
                true
            }
            val openLinksAutomaticallyPreference = findPreference<SwitchPreferenceCompat>(getString(R.string.key_open_links_automatically))
            openLinksAutomaticallyPreference?.setOnPreferenceChangeListener { _, newValue ->
                settings.openLinksAutomatically = newValue as Boolean
                true
            }
            val saveScannedBarcodesPreference = findPreference<SwitchPreferenceCompat>(getString(R.string.key_save_scanned_barcodes))
            saveScannedBarcodesPreference?.setOnPreferenceChangeListener { _, newValue ->
                settings.saveScannedBarcodesToHistory = newValue as Boolean
                true
            }
            val saveCreatedBarcodesPreference = findPreference<SwitchPreferenceCompat>(getString(R.string.key_save_created_barcodes))
            saveCreatedBarcodesPreference?.setOnPreferenceChangeListener { _, newValue ->
                settings.saveCreatedBarcodesToHistory = newValue as Boolean
                true
            }
            val confirmScansManuallyPreference = findPreference<SwitchPreferenceCompat>(getString(R.string.key_confirm_scans_manually))
            confirmScansManuallyPreference?.setOnPreferenceChangeListener { _, newValue ->
                settings.confirmScansManually = newValue as Boolean
                true
            }
            val changelogPreference = findPreference<Preference>(getString(R.string.key_changelog))
            changelogPreference?.setOnPreferenceClickListener {
                MaterialAlertDialogBuilder(requireContext())
                    .setTitle(requireContext().getString(R.string.changelog_title, BuildConfig.VERSION_NAME))
                    .setIcon(R.drawable.ic_changelog)
                    .setMessage(R.string.changes)
                    .setNegativeButton(android.R.string.cancel, null)
                    .show()
                true
            }
            val notificationsSettings = findPreference<Preference>(getString(R.string.key_notifications_settings))
            notificationsSettings?.setOnPreferenceClickListener {
                val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
                intent.putExtra(Settings.EXTRA_APP_PACKAGE, context?.packageName)
                startActivity(intent)
                true
            }
            val sharePreference = findPreference<Preference>(getString(R.string.key_share))
            sharePreference?.setOnPreferenceClickListener {
                val sharingIntent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID)
                    putExtra(Intent.EXTRA_SUBJECT, R.string.share_subject)
                }
                startActivity(Intent.createChooser(sharingIntent, getString(R.string.share_using)))
                true
            }
            val ossPreference = findPreference<Preference>(getString(R.string.key_open_source_licenses))
            ossPreference?.setOnPreferenceClickListener {
                startActivity(Intent(activity, OssLicensesMenuActivity::class.java))
                true
            }
            val deviceInfoPreference = findPreference<Preference>(getString(R.string.key_device_info))
            val version = String.format(
                resources.getString(R.string.app_build),
                "${resources.getString(R.string.manufacturer)} ${Build.MANUFACTURER}",
                "${resources.getString(R.string.device_model)} ${Build.MODEL}",
                "${resources.getString(R.string.android_version)} ${Build.VERSION.RELEASE}",
                "${resources.getString(R.string.api_level)} ${Build.VERSION.SDK_INT}",
                "${resources.getString(R.string.arch)} ${Build.SUPPORTED_ABIS.joinToString()}"
            )
            deviceInfoPreference?.summary = version
            deviceInfoPreference?.setOnPreferenceClickListener {
                val clipboard = requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clip = ClipData.newPlainText("text", version)
                clipboard.setPrimaryClip(clip)
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.S_V2) {
                    Snackbar.make(requireView(), R.string.snack_copied_to_clipboard, Snackbar.LENGTH_LONG).show()
                }
                true
            }
            val cleanHistoryPreference = findPreference<Preference>(getString(R.string.key_clean_history))
            cleanHistoryPreference?.setOnPreferenceClickListener {
                val dialog = DeleteConfirmationDialogFragment.newInstance(R.string.summary_delete_history)
                dialog.show(childFragmentManager, "")
                true
            }
        }
    }
}
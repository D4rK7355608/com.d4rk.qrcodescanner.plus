package com.d4rk.qrcodescanner.plus.feature.tabs.settings.theme
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.d4rk.qrcodescanner.plus.databinding.ActivityChooseThemeBinding
import com.d4rk.qrcodescanner.plus.di.settings
import com.d4rk.qrcodescanner.plus.extension.applySystemWindowInsets
import com.d4rk.qrcodescanner.plus.extension.unsafeLazy
import com.d4rk.qrcodescanner.plus.feature.BaseActivity
import com.d4rk.qrcodescanner.plus.usecase.Settings
class ChooseThemeActivity : BaseActivity() {
    private lateinit var binding: ActivityChooseThemeBinding
    companion object {
        fun start(context: Context) {
            val intent = Intent(context, ChooseThemeActivity::class.java)
            context.startActivity(intent)
        }
    }
    private val buttons by unsafeLazy {
        listOf(binding.buttonSystemTheme, binding.buttonLightTheme, binding.buttonDarkTheme)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChooseThemeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportEdgeToEdge()
        initToolbar()
    }
    override fun onResume() {
        super.onResume()
        showInitialSettings()
        handleSettingsChanged()
    }
    private fun supportEdgeToEdge() {
        binding.rootView.applySystemWindowInsets(applyTop = true, applyBottom = true)
    }
    private fun initToolbar() {
        binding.toolbar.setNavigationOnClickListener { finish() }
    }
    private fun showInitialSettings() {
        val theme = settings.theme
        binding.buttonSystemTheme.isChecked = theme == Settings.THEME_SYSTEM
        binding.buttonLightTheme.isChecked = theme == Settings.THEME_LIGHT
        binding.buttonDarkTheme.isChecked = theme == Settings.THEME_DARK
    }
    private fun handleSettingsChanged() {
        binding.buttonSystemTheme.setCheckedChangedListener { isChecked ->
            if (isChecked.not()) {
                return@setCheckedChangedListener
            }
            uncheckOtherButtons(binding.buttonSystemTheme)
            settings.theme = Settings.THEME_SYSTEM
        }
        binding.buttonLightTheme.setCheckedChangedListener { isChecked ->
            if (isChecked.not()) {
                return@setCheckedChangedListener
            }
            uncheckOtherButtons(binding.buttonLightTheme)
            settings.theme = Settings.THEME_LIGHT
        }
        binding.buttonDarkTheme.setCheckedChangedListener { isChecked ->
            if (isChecked.not()) {
                return@setCheckedChangedListener
            }
            uncheckOtherButtons(binding.buttonDarkTheme)
            settings.theme = Settings.THEME_DARK
        }
    }
    private fun uncheckOtherButtons(checkedButton: View) {
        buttons.forEach { button ->
            if (checkedButton !== button) {
                button.isChecked = false
            }
        }
    }
}
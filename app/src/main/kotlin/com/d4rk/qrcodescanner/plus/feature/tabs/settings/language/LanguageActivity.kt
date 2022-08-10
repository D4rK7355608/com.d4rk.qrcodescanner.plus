package com.d4rk.qrcodescanner.plus.feature.tabs.settings.language
import android.os.Bundle
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.d4rk.qrcodescanner.plus.R
import com.d4rk.qrcodescanner.plus.databinding.ActivityLanguageBinding
import com.kieronquinn.monetcompat.app.MonetCompatActivity
import java.util.Locale
class LanguageActivity : MonetCompatActivity() {
    private lateinit var binding: ActivityLanguageBinding
    var context: Context? = null
    private lateinit var locale: Locale
    private var currentLanguage = "en"
    private var currentLang: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLanguageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val sharedPreferences = getSharedPreferences("save", MODE_PRIVATE)
        binding.defaultLanguage.isChecked = sharedPreferences.getBoolean("value", true)
        binding.defaultLanguage.setOnCheckedChangeListener { _, _ ->
            if (binding.defaultLanguage.isChecked) {
                for (index in 0 until binding.languageGroup.childCount) {
                    binding.languageGroup.getChildAt(index).isEnabled = false
                }
                setLocale("en")
                val editor = getSharedPreferences("save", MODE_PRIVATE).edit()
                editor.putBoolean("value", true)
                editor.apply()
            } else {
                for (index in 0 until binding.languageGroup.childCount) {
                    binding.languageGroup.getChildAt(index).isEnabled = true
                }
                val editor = getSharedPreferences("save", MODE_PRIVATE).edit()
                editor.putBoolean("value", false)
                editor.apply()
            }
        }
        if(binding.defaultLanguage.isChecked) {
            for (index in 0 until binding.languageGroup.childCount) {
                binding.languageGroup.getChildAt(index).isEnabled = false
            }
        } else {
            for (index in 0 until binding.languageGroup.childCount) {
                binding.languageGroup.getChildAt(index).isEnabled = true
            }
        }
        val preferences = getSharedPreferences("saved", 0)
        binding.languageGroup.check(preferences.getInt("CheckedId", 0))
        binding.languageGroup.setOnCheckedChangeListener { _, checkedId ->
            val editor = preferences.edit()
            editor.putInt("CheckedId", checkedId)
            editor.apply()
        }
        binding.romanianButton.setOnClickListener {
            setLocale("ro")
        }
        binding.englishButton.setOnClickListener {
            setLocale("en")
        }
        currentLanguage = intent.getStringExtra(currentLang).toString()
    }
    @Suppress("DEPRECATION")
    private fun setLocale(localeName: String) {
        if (localeName != currentLanguage) {
            locale = Locale(localeName)
            val res = resources
            val dm = res.displayMetrics
            val conf = res.configuration
            conf.locale = locale
            res.updateConfiguration(conf, dm)
            val refresh = Intent(this, LanguageActivity::class.java)
            refresh.putExtra(currentLang, localeName)
            startActivity(refresh)
        } else {
            Toast.makeText(this, R.string.language_selected, Toast.LENGTH_SHORT).show()
        }
    }
}
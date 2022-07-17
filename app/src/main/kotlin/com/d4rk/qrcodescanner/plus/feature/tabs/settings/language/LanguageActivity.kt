package com.d4rk.qrcodescanner.plus.feature.tabs.settings.language
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.d4rk.qrcodescanner.plus.databinding.ActivityLanguageBinding
class LanguageActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLanguageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLanguageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val sharedPreferences = getSharedPreferences("save", MODE_PRIVATE)
        binding.defaultLanguage.isChecked = sharedPreferences.getBoolean("value", true)
        val checkRefreshDefaultLanguage: Boolean? = null
        if (checkRefreshDefaultLanguage == false) {
            val editor = getSharedPreferences("save", MODE_PRIVATE).edit()
            editor.putBoolean("value", false)
            editor.apply()
            binding.defaultLanguage.isChecked = false
            for (i in 0 until binding.languageGroup.childCount) {
                binding.languageGroup.getChildAt(i).isEnabled = true
            }
        } else {
            val editor = getSharedPreferences("save", MODE_PRIVATE).edit()
            editor.putBoolean("value", true)
            editor.apply()
            binding.defaultLanguage.isChecked = true
            for (i in 0 until binding.languageGroup.childCount) {
                binding.languageGroup.getChildAt(i).isEnabled = false
            }
        }
        binding.defaultLanguage.setOnCheckedChangeListener { _, _ ->
            if (binding.defaultLanguage.isChecked)
            {
                val editor = getSharedPreferences("save", MODE_PRIVATE).edit()
                editor.putBoolean("value", true)
                editor.apply()
                binding.defaultLanguage.isChecked = true
                for (i in 0 until binding.languageGroup.childCount) {
                    binding.languageGroup.getChildAt(i).isEnabled = false
                }
            } else {
                val editor = getSharedPreferences("save", MODE_PRIVATE).edit()
                editor.putBoolean("value", false)
                editor.apply()
                binding.defaultLanguage.isChecked = false
                for (i in 0 until binding.languageGroup.childCount) {
                    binding.languageGroup.getChildAt(i).isEnabled = true
                }
            }
        }
    }
}
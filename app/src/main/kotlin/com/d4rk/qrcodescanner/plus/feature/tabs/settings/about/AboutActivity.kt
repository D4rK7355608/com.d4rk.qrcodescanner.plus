package com.d4rk.qrcodescanner.plus.feature.tabs.settings.about
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.d4rk.qrcodescanner.plus.BuildConfig
import com.d4rk.qrcodescanner.plus.R
import com.d4rk.qrcodescanner.plus.databinding.ActivityAboutBinding
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
class AboutActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAboutBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAboutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val content = getString(R.string.app_version, BuildConfig.VERSION_NAME)
        binding.itemSettingsMoreAboutVersion.text = content
        binding.itemSettingsMoreAboutIcon.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://sites.google.com/view/d4rk7355608"))
            startActivity(intent)
        }
        binding.itemSettingsMoreAboutIcon.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://sites.google.com/view/d4rk7355608"))
            startActivity(intent)
        }
        binding.itemSettingsMoreAboutGoogleDev.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://developers.google.com/profile/u/D4rK7355608"))
            startActivity(intent)
        }
        binding.itemSettingsMoreAboutYoutube.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/c/D4rK7355608"))
            startActivity(intent)
        }
        binding.itemSettingsMoreAboutGithub.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/D4rK7355608/com.d4rk.qrcodescanner.plus"))
            startActivity(intent)
        }
        binding.itemSettingsMoreAboutTwitter.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/D4rK7355608"))
            startActivity(intent)
        }
        binding.itemSettingsMoreAboutXda.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://forum.xda-developers.com/m/d4rk7355608.10095012"))
            startActivity(intent)
        }
        binding.itemSettingsMoreAboutLibraries.setOnClickListener {
            val intent = Intent(this, OssLicensesMenuActivity::class.java)
            startActivity(intent)
        }
    }
}
package com.d4rk.qrcodescanner.plus.ui.about
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.d4rk.qrcodescanner.plus.BuildConfig
import com.d4rk.qrcodescanner.plus.R
import com.d4rk.qrcodescanner.plus.databinding.FragmentAboutBinding
import com.d4rk.qrcodescanner.plus.ui.viewmodel.ViewModel
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.android.material.snackbar.Snackbar
import me.zhanghai.android.fastscroll.FastScrollerBuilder
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AboutFragment : Fragment() {
    private lateinit var binding: FragmentAboutBinding
    private val calendar: Calendar = Calendar.getInstance()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        ViewModelProvider(this)[ViewModel::class.java]
        binding = FragmentAboutBinding.inflate(inflater, container, false)
        FastScrollerBuilder(binding.scrollView).useMd2Style().build()
        MobileAds.initialize(requireContext())
        binding.adView.loadAd(AdRequest.Builder().build())
        val version = String.format(resources.getString(R.string.app_version), BuildConfig.VERSION_NAME)
        binding.textViewAppVersion.text = version
        val simpleDateFormat = SimpleDateFormat("yyyy", Locale.getDefault())
        val dateText = simpleDateFormat.format(calendar.time)
        val copyright = requireContext().getString(R.string.copyright, dateText)
        binding.textViewCopyright.text = copyright
        binding.textViewAppVersion.setOnLongClickListener {
            val clipboardManager = requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clipData: ClipData = ClipData.newPlainText("Label", binding.textViewAppVersion.text)
            clipboardManager.setPrimaryClip(clipData)
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.S_V2)
                Snackbar.make(requireView(), R.string.snack_copied_to_clipboard, Snackbar.LENGTH_SHORT).show()
            true
        }
        binding.imageViewAppIcon.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW,
                "https://sites.google.com/view/d4rk7355608".toUri()))
        }
        binding.chipGoogleDev.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, "https://g.dev/D4rK7355608".toUri()))
        }
        binding.chipYoutube.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, "https://www.youtube.com/c/D4rK7355608".toUri()))
        }
        binding.chipGithub.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW,
                ("https://github.com/D4rK7355608/" + BuildConfig.APPLICATION_ID).toUri()))
        }
        binding.chipTwitter.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, "https://twitter.com/D4rK7355608".toUri()))
        }
        binding.chipXda.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW,
                "https://forum.xda-developers.com/m/d4rk7355608.10095012".toUri()
            ))
        }
        binding.chipMusic.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW,
                "https://sites.google.com/view/d4rk7355608/tracks".toUri()
            ))
        }
        return binding.root
    }
}
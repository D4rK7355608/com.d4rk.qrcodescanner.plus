package com.d4rk.qrcodescanner.plus.feature.tabs
import android.os.Bundle
import android.view.MenuItem
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.fragment.app.Fragment
import androidx.multidex.BuildConfig
import com.d4rk.qrcodescanner.plus.R
import com.d4rk.qrcodescanner.plus.ads.Ads
import com.d4rk.qrcodescanner.plus.databinding.ActivityBottomTabsBinding
import com.d4rk.qrcodescanner.plus.databinding.FragmentCreateBarcodeBinding
import com.d4rk.qrcodescanner.plus.databinding.FragmentSettingsBinding
import com.d4rk.qrcodescanner.plus.di.settings
import com.d4rk.qrcodescanner.plus.extension.applySystemWindowInsets
import com.d4rk.qrcodescanner.plus.feature.BaseActivity
import com.d4rk.qrcodescanner.plus.feature.tabs.create.CreateBarcodeFragment
import com.d4rk.qrcodescanner.plus.feature.tabs.history.BarcodeHistoryFragment
import com.d4rk.qrcodescanner.plus.feature.tabs.scan.ScanBarcodeFromCameraFragment
import com.d4rk.qrcodescanner.plus.feature.tabs.settings.SettingsFragment
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.FirebaseApp
@Suppress("DEPRECATION")
class BottomTabsActivity : BaseActivity(), BottomNavigationView.OnNavigationItemSelectedListener {
    private lateinit var binding: ActivityBottomTabsBinding
    private lateinit var settingsAdBinding: FragmentSettingsBinding
    private lateinit var createAdBinding: FragmentCreateBarcodeBinding
    companion object {
        private const val ACTION_CREATE_BARCODE = "${BuildConfig.APPLICATION_ID}.CREATE_BARCODE"
        private const val ACTION_HISTORY = "${BuildConfig.APPLICATION_ID}.HISTORY"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        FirebaseApp.initializeApp(this)
        MobileAds.initialize(this)
        settings.reapplyTheme()
        binding = ActivityBottomTabsBinding.inflate(layoutInflater)
        settingsAdBinding = FragmentSettingsBinding.inflate(layoutInflater)
        createAdBinding = FragmentCreateBarcodeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportEdgeToEdge()
        initBottomNavigationView()
        val adRequest = AdRequest.Builder().build()
        settingsAdBinding.settingsAdView.loadAd(adRequest)
        settingsAdBinding.settingsAdView.loadAd(adRequest)
        createAdBinding.createAdView.loadAd(adRequest)
        createAdBinding.createAdView.loadAd(adRequest)
        if (savedInstanceState == null) {
            showInitialFragment()
        }
        val application = application as? Ads ?: return
        application.showAdIfAvailable(this, object : Ads.OnShowAdCompleteListener {
            override fun onShowAdComplete() {
            }
        })
    }
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        if (item.itemId == binding.bottomNavigationView.selectedItemId) {
            return false
        }
        showFragment(item.itemId)
        return true
    }
    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        moveTaskToBack(true)
        if (binding.bottomNavigationView.selectedItemId == R.id.item_scan) {
            super.onBackPressed()
        } else {
            binding.bottomNavigationView.selectedItemId = R.id.item_scan
        }
    }
    private fun supportEdgeToEdge() {
        binding.bottomNavigationView.applySystemWindowInsets(applyBottom = true)
    }
    private fun initBottomNavigationView() {
        binding.bottomNavigationView.apply {
            setOnNavigationItemSelectedListener(this@BottomTabsActivity)
        }
    }
    private fun showInitialFragment() {
        when (intent?.action) {
            ACTION_CREATE_BARCODE -> binding.bottomNavigationView.selectedItemId = R.id.item_create
            ACTION_HISTORY -> binding.bottomNavigationView.selectedItemId = R.id.item_history
            else -> showFragment(R.id.item_scan)
        }
    }
    private fun showFragment(bottomItemId: Int) {
        val fragment = when (bottomItemId) {
            R.id.item_scan -> ScanBarcodeFromCameraFragment()
            R.id.item_create -> CreateBarcodeFragment()
            R.id.item_history -> BarcodeHistoryFragment()
            R.id.item_settings -> SettingsFragment()
            else -> null
        }
        fragment?.apply(::replaceFragment)
    }
    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.layout_fragment_container, fragment)
            .setReorderingAllowed(true)
            .commit()
    }
    public override fun onPause() {
        settingsAdBinding.settingsAdView.pause()
        createAdBinding.createAdView.pause()
        super.onPause()
    }
    public override fun onResume() {
        super.onResume()
        settingsAdBinding.settingsAdView.resume()
        createAdBinding.createAdView.resume()
    }
    public override fun onDestroy() {
        settingsAdBinding.settingsAdView.destroy()
        createAdBinding.createAdView.destroy()
        super.onDestroy()
    }
}
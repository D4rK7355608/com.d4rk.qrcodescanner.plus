package com.d4rk.qrcodescanner.plus.ui
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.preference.PreferenceManager
import com.d4rk.qrcodescanner.plus.R
import com.d4rk.qrcodescanner.plus.databinding.ActivityMainBinding
import com.d4rk.qrcodescanner.plus.notifications.AppUpdateNotificationsManager
import com.d4rk.qrcodescanner.plus.notifications.AppUsageNotificationsManager
import com.d4rk.qrcodescanner.plus.ui.settings.SettingsActivity
import com.d4rk.qrcodescanner.plus.ui.settings.support.SupportActivity
import com.d4rk.qrcodescanner.plus.ui.startup.StartupActivity
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.navigation.NavigationBarView
import com.google.android.material.snackbar.Snackbar
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.ActivityResult
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var appUpdateManager: AppUpdateManager
    private val requestUpdateCode = 1
    private lateinit var appUpdateNotificationsManager: AppUpdateNotificationsManager
    private val handler = Handler(Looper.getMainLooper())
    private val snackbarInterval: Long = 60L * 24 * 60 * 60 * 1000
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        MobileAds.initialize(this)
        binding.adView.loadAd(AdRequest.Builder().build())
        appUpdateManager = AppUpdateManagerFactory.create(this)
        appUpdateManager = AppUpdateManagerFactory.create(this)
        appUpdateNotificationsManager = AppUpdateNotificationsManager(this)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        applyAppSettings()
        handler.postDelayed(::showSnackbar, snackbarInterval)
    }
    private fun applyAppSettings() {
        val themeValues = resources.getStringArray(R.array.preference_theme_values)
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        val defaultTabKey = getString(R.string.key_default_tab)
        val defaultTabValue = getString(R.string.default_value_tab)
        val defaultTabValues = resources.getStringArray(R.array.preference_default_tab_values)
        val bottomNavigationBarLabelsKey = getString(R.string.key_bottom_navigation_bar_labels)
        val bottomNavigationBarLabelsValues = resources.getStringArray(R.array.preference_bottom_navigation_bar_labels_values)
        val labelDefaultValue = getString(R.string.default_value_bottom_navigation_bar_labels)
        when (sharedPreferences.getString(getString(R.string.key_theme), getString(R.string.default_value_theme))) {
            themeValues[0] -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            themeValues[1] -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            themeValues[2] -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            themeValues[3] -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY)
        }
        val languageCode = sharedPreferences.getString(getString(R.string.key_language), getString(R.string.default_value_language))
        AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(languageCode))

        val navController by lazy {
            val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
            navHostFragment.navController
        }
        val startFragmentId = when (sharedPreferences.getString(defaultTabKey, defaultTabValue)) {
            defaultTabValues[0] -> R.id.navigation_scan
            defaultTabValues[1] -> R.id.navigation_create
            defaultTabValues[2] -> R.id.navigation_history
            defaultTabValues[3] -> R.id.navigation_about
            else -> R.id.navigation_scan
        }
        navController.graph.setStartDestination(startFragmentId)
        navController.navigate(startFragmentId)
        binding.navView.setupWithNavController(navController)
        binding.navView.labelVisibilityMode = when (sharedPreferences.getString(bottomNavigationBarLabelsKey, labelDefaultValue)) {
            bottomNavigationBarLabelsValues[0] -> NavigationBarView.LABEL_VISIBILITY_LABELED
            bottomNavigationBarLabelsValues[1] -> NavigationBarView.LABEL_VISIBILITY_SELECTED
            bottomNavigationBarLabelsValues[2] -> NavigationBarView.LABEL_VISIBILITY_UNLABELED
            else -> NavigationBarView.LABEL_VISIBILITY_AUTO
        }
        val appBarConfiguration = AppBarConfiguration(setOf(R.id.navigation_scan, R.id.navigation_create, R.id.navigation_history, R.id.navigation_about))
        setupActionBarWithNavController(navController, appBarConfiguration)
    }
    private fun showSnackbar() {
        Snackbar.make(binding.root, getString(R.string.snack_support), Snackbar.LENGTH_LONG)
            .setAction(getString(android.R.string.ok)) {
                val intent = Intent(this, SupportActivity::class.java)
                startActivity(intent)
            }
            .show()
        handler.postDelayed(::showSnackbar, snackbarInterval)
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.settings -> {
                startActivity(Intent(this, SettingsActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    @Deprecated("Deprecated in Java")
    @Suppress("DEPRECATION")
    override fun onBackPressed() {
        MaterialAlertDialogBuilder(this)
            .setTitle(R.string.close)
            .setMessage(R.string.summary_close)
            .setPositiveButton(android.R.string.yes) { _, _ ->
                super.onBackPressed()
                moveTaskToBack(true)
            }
            .setNegativeButton(android.R.string.no, null)
            .apply { show() }
    }
    override fun onResume() {
        super.onResume()
        if (!PreferenceManager.getDefaultSharedPreferences(this).getBoolean(getString(R.string.key_firebase), true)) {
            FirebaseAnalytics.getInstance(this).setAnalyticsCollectionEnabled(false)
            FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(false)
        } else {
            FirebaseAnalytics.getInstance(this).setAnalyticsCollectionEnabled(true)
            FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true)
        }
        val appUsageNotificationsManager = AppUsageNotificationsManager(this)
        appUsageNotificationsManager.checkAndSendAppUsageNotification()
        appUpdateNotificationsManager.checkAndSendUpdateNotification()
        appUpdateManager.appUpdateInfo.addOnSuccessListener { appUpdateInfo ->
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)) {
                @Suppress("DEPRECATION")
                appUpdateManager.startUpdateFlowForResult(appUpdateInfo, AppUpdateType.FLEXIBLE, this, requestUpdateCode)
            }
        }
        startupScreen()
    }
    private fun startupScreen() {
        val startupPreference = getSharedPreferences("startup", MODE_PRIVATE)
        if (startupPreference.getBoolean("value", true)) {
            startupPreference.edit().putBoolean("value", false).apply()
            startActivity(Intent(this, StartupActivity::class.java))
        }
    }
    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == requestUpdateCode) {
            when (resultCode) {
                RESULT_OK -> {
                }
                RESULT_CANCELED -> {
                }
                ActivityResult.RESULT_IN_APP_UPDATE_FAILED -> {
                }
            }
        }
    }
}
package com.d4rk.android.libs.apptoolkit.data.datastore

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.d4rk.android.libs.apptoolkit.core.utils.constants.datastore.DataStoreNamesConstants
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.commonDataStore : DataStore<Preferences> by preferencesDataStore(name = DataStoreNamesConstants.DATA_STORE_SETTINGS)

/**
 * A singleton class responsible for managing application-wide data using Android DataStore.
 *
 * This class provides methods to access and modify various application settings and data,
 * such as last used timestamp, startup status, theme preferences, language, and usage & diagnostics settings.
 *
 * The DataStore is backed by a file named "common_data_store" within the application's data directory.
 *
 * @property dataStore The DataStore instance for storing preferences.
 */
open class CommonDataStore(context : Context) {
    val dataStore : DataStore<Preferences> = context.commonDataStore

    companion object {
        @Volatile
        private var instance : CommonDataStore? = null

        fun getInstance(context : Context) : CommonDataStore {
            return instance ?: synchronized(lock = this) {
                instance ?: CommonDataStore(context.applicationContext).also { instance = it }
            }
        }
    }

    // Last used app notifications
    private val lastUsedKey = longPreferencesKey(name = DataStoreNamesConstants.DATA_STORE_LAST_USED)
    val lastUsed : Flow<Long> = dataStore.data.map { preferences : Preferences ->
        preferences[lastUsedKey] ?: 0
    }

    suspend fun saveLastUsed(timestamp : Long) {
        dataStore.edit { preferences : MutablePreferences ->
            preferences[lastUsedKey] = timestamp
        }
    }

    // Startup
    private val startupKey = booleanPreferencesKey(name = DataStoreNamesConstants.DATA_STORE_STARTUP)
    val startup : Flow<Boolean> = dataStore.data.map { preferences : Preferences ->
        preferences[startupKey] != false
    }

    suspend fun saveStartup(isFirstTime : Boolean) {
        dataStore.edit { preferences : MutablePreferences ->
            preferences[startupKey] = isFirstTime
        }
    }

    // Display
    val themeModeState = mutableStateOf(value = DataStoreNamesConstants.THEME_MODE_FOLLOW_SYSTEM)
    private val themeModeKey = stringPreferencesKey(name = DataStoreNamesConstants.DATA_STORE_THEME_MODE)
    val themeMode : Flow<String> = dataStore.data.map { preferences : Preferences ->
        preferences[themeModeKey] ?: DataStoreNamesConstants.THEME_MODE_FOLLOW_SYSTEM
    }

    suspend fun saveThemeMode(mode : String) {
        dataStore.edit { preferences : MutablePreferences ->
            preferences[themeModeKey] = mode
        }
    }

    private val amoledModeKey = booleanPreferencesKey(name = DataStoreNamesConstants.DATA_STORE_AMOLED_MODE)
    val amoledMode : Flow<Boolean> = dataStore.data.map { preferences : Preferences ->
        preferences[amoledModeKey] == true
    }

    suspend fun saveAmoledMode(isChecked : Boolean) {
        dataStore.edit { preferences : MutablePreferences ->
            preferences[amoledModeKey] = isChecked
        }
    }

    private val dynamicColorsKey = booleanPreferencesKey(name = DataStoreNamesConstants.DATA_STORE_DYNAMIC_COLORS)
    val dynamicColors : Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[dynamicColorsKey] != false
    }

    suspend fun saveDynamicColors(isChecked : Boolean) {
        dataStore.edit { preferences : MutablePreferences ->
            preferences[dynamicColorsKey] = isChecked
        }
    }

    private val bouncyButtonsKey = booleanPreferencesKey(name = DataStoreNamesConstants.DATA_STORE_BOUNCY_BUTTONS)
    val bouncyButtons : Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[bouncyButtonsKey] != false
    }

    suspend fun saveBouncyButtons(isChecked : Boolean) {
        dataStore.edit { preferences : MutablePreferences ->
            preferences[bouncyButtonsKey] = isChecked
        }
    }

    private val languageKey = stringPreferencesKey(name = DataStoreNamesConstants.DATA_STORE_LANGUAGE)

    fun getLanguage() : Flow<String> = dataStore.data.map { preferences : Preferences ->
        preferences[languageKey] ?: "en"
    }

    suspend fun saveLanguage(language : String) {
        dataStore.edit { preferences : MutablePreferences ->
            preferences[languageKey] = language
        }
    }

    // Usage and Diagnostics
    private val usageAndDiagnosticsKey : Preferences.Key<Boolean> = booleanPreferencesKey(name = DataStoreNamesConstants.DATA_STORE_USAGE_AND_DIAGNOSTICS)
    fun usageAndDiagnostics(default : Boolean) : Flow<Boolean> = dataStore.data.map { preferences : Preferences ->
        preferences[usageAndDiagnosticsKey] ?: default
    }

    suspend fun saveUsageAndDiagnostics(isChecked : Boolean) {
        dataStore.edit { preferences : MutablePreferences -> preferences[usageAndDiagnosticsKey] = isChecked }
    }

    // Analytics Consent
    private val analyticsConsentKey : Preferences.Key<Boolean> = booleanPreferencesKey(name = DataStoreNamesConstants.DATA_STORE_ANALYTICS_CONSENT)
    fun analyticsConsent(default : Boolean) : Flow<Boolean> = dataStore.data.map { preferences : Preferences ->
        preferences[analyticsConsentKey] ?: default
    }

    suspend fun saveAnalyticsConsent(isGranted : Boolean) {
        dataStore.edit { preferences : MutablePreferences -> preferences[analyticsConsentKey] = isGranted }
    }

    // Ad Storage Consent
    private val adStorageConsentKey : Preferences.Key<Boolean> = booleanPreferencesKey(name = DataStoreNamesConstants.DATA_STORE_AD_STORAGE_CONSENT)
    fun adStorageConsent(default : Boolean) : Flow<Boolean> = dataStore.data.map { preferences : Preferences ->
        preferences[adStorageConsentKey] ?: default
    }

    suspend fun saveAdStorageConsent(isGranted : Boolean) {
        dataStore.edit { preferences : MutablePreferences -> preferences[adStorageConsentKey] = isGranted }
    }

    // Ad User Data Consent
    private val adUserDataConsentKey : Preferences.Key<Boolean> = booleanPreferencesKey(name = DataStoreNamesConstants.DATA_STORE_AD_USER_DATA_CONSENT)
    fun adUserDataConsent(default : Boolean) : Flow<Boolean> = dataStore.data.map { preferences : Preferences ->
        preferences[adUserDataConsentKey] ?: default
    }

    suspend fun saveAdUserDataConsent(isGranted : Boolean) {
        dataStore.edit { preferences : MutablePreferences -> preferences[adUserDataConsentKey] = isGranted }
    }

    // Ad Personalization Consent
    private val adPersonalizationConsentKey : Preferences.Key<Boolean> = booleanPreferencesKey(name = DataStoreNamesConstants.DATA_STORE_AD_PERSONALIZATION_CONSENT)
    fun adPersonalizationConsent(default : Boolean) : Flow<Boolean> = dataStore.data.map { preferences : Preferences ->
        preferences[adPersonalizationConsentKey] ?: default
    }

    suspend fun saveAdPersonalizationConsent(isGranted : Boolean) {
        dataStore.edit { preferences : MutablePreferences -> preferences[adPersonalizationConsentKey] = isGranted }
    }

    // Ads
    private val adsKey = booleanPreferencesKey(name = DataStoreNamesConstants.DATA_STORE_ADS)
    fun ads(default : Boolean) : Flow<Boolean> = dataStore.data.map { prefs : Preferences ->
        prefs[adsKey] ?: default
    }

    suspend fun saveAds(isChecked : Boolean) {
        dataStore.edit { preferences : MutablePreferences ->
            preferences[adsKey] = isChecked
        }
    }

    // App Review
    private val reviewDoneKey = booleanPreferencesKey(name = DataStoreNamesConstants.DATA_STORE_REVIEW_DONE)
    val reviewDone : Flow<Boolean> = dataStore.data.map { prefs : Preferences ->
        prefs[reviewDoneKey] == true
    }

    suspend fun saveReviewDone(isDone : Boolean) {
        dataStore.edit { preferences : MutablePreferences ->
            preferences[reviewDoneKey] = isDone
        }
    }

    // Review Prompt
    private val sessionCountKey = longPreferencesKey(name = DataStoreNamesConstants.DATA_STORE_SESSION_COUNT)
    val sessionCount : Flow<Int> = dataStore.data.map { prefs : Preferences ->
        (prefs[sessionCountKey] ?: 0L).toInt()
    }

    private val reviewPromptedKey = booleanPreferencesKey(name = DataStoreNamesConstants.DATA_STORE_REVIEW_PROMPTED)
    val hasPromptedReview : Flow<Boolean> = dataStore.data.map { prefs : Preferences ->
        prefs[reviewPromptedKey] == true
    }

    suspend fun incrementSessionCount() {
        dataStore.edit { prefs : MutablePreferences ->
            val current : Long = prefs[sessionCountKey] ?: 0L
            prefs[sessionCountKey] = current + 1L
        }
    }

    suspend fun setHasPromptedReview(value : Boolean) {
        dataStore.edit { prefs : MutablePreferences ->
            prefs[reviewPromptedKey] = value
        }
    }
}
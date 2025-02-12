package com.d4rk.qrcodescanner.plus.data.datastore

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.d4rk.android.libs.apptoolkit.data.datastore.CommonDataStore
import com.d4rk.qrcodescanner.plus.model.SearchEngine
import com.d4rk.qrcodescanner.plus.utils.constants.ui.bottombar.BottomBarRoutes
import com.google.zxing.BarcodeFormat
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DataStore(context : Context) : CommonDataStore(context) {

    companion object {
        @Volatile
        private var instance : DataStore? = null

        fun getInstance(context : Context) : DataStore {
            return instance ?: synchronized(this) {
                instance ?: DataStore(context.applicationContext).also { instance = it }
            }
        }

        private val INVERSE_BARCODE_COLORS = booleanPreferencesKey("inverse_barcode_colors")
        private val OPEN_LINKS_AUTOMATICALLY = booleanPreferencesKey("open_links_automatically")
        private val COPY_TO_CLIPBOARD = booleanPreferencesKey("copy_to_clipboard")
        private val SIMPLE_AUTO_FOCUS = booleanPreferencesKey("simple_auto_focus")
        private val FLASHLIGHT = booleanPreferencesKey("flashlight")
        private val VIBRATE = booleanPreferencesKey("vibrate")
        private val CONTINUOUS_SCANNING = booleanPreferencesKey("continuous_scanning")
        private val CONFIRM_SCANS_MANUALLY = booleanPreferencesKey("confirm_scans_manually")
        private val IS_BACK_CAMERA = booleanPreferencesKey("is_back_camera")
        private val SAVE_SCANNED_BARCODES_TO_HISTORY =
                booleanPreferencesKey("save_scanned_barcodes_to_history")
        private val SAVE_CREATED_BARCODES_TO_HISTORY =
                booleanPreferencesKey("save_created_barcodes_to_history")
        private val DO_NOT_SAVE_DUPLICATES = booleanPreferencesKey("do_not_save_duplicates")
        private val SEARCH_ENGINE = stringPreferencesKey("search_engine")
    }

    fun getStartupPage() : Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[stringPreferencesKey(name = "startup_page")] ?: BottomBarRoutes.SCAN
        }
    }

    suspend fun saveStartupPage(startupPage : String) {
        dataStore.edit { preferences ->
            preferences[stringPreferencesKey(name = "startup_page")] = startupPage
        }
    }

    fun getShowBottomBarLabels() : Flow<Boolean> {
        return dataStore.data.map { preferences ->
            preferences[booleanPreferencesKey(name = "show_bottom_bar_labels")] ?: true
        }
    }

    suspend fun setShowBottomBarLabels(showLabels : Boolean) {
        dataStore.edit { preferences ->
            preferences[booleanPreferencesKey(name = "show_bottom_bar_labels")] = showLabels
        }
    }

    // O pula de scanare

    // Barcode Colors
    var areBarcodeColorsInversed : Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[INVERSE_BARCODE_COLORS] ?: false
    }

    suspend fun saveBarcodeColorsInversed(isInversed : Boolean) {
        dataStore.edit { preferences ->
            preferences[INVERSE_BARCODE_COLORS] = isInversed
        }
    }

    // Open Links Automatically
    var openLinksAutomatically : Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[OPEN_LINKS_AUTOMATICALLY] ?: false
    }

    suspend fun saveOpenLinksAutomatically(isOpenAutomatically : Boolean) {
        dataStore.edit { preferences ->
            preferences[OPEN_LINKS_AUTOMATICALLY] = isOpenAutomatically
        }
    }

    // Copy to Clipboard
    var copyToClipboard : Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[COPY_TO_CLIPBOARD] ?: true
    }

    suspend fun saveCopyToClipboard(isCopyToClipboard : Boolean) {
        dataStore.edit { preferences ->
            preferences[COPY_TO_CLIPBOARD] = isCopyToClipboard
        }
    }

    // Simple Auto Focus
    var simpleAutoFocus : Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[SIMPLE_AUTO_FOCUS] ?: false
    }

    suspend fun saveSimpleAutoFocus(isSimpleAutoFocus : Boolean) {
        dataStore.edit { preferences ->
            preferences[SIMPLE_AUTO_FOCUS] = isSimpleAutoFocus
        }
    }

    // Flashlight
    var flash : Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[FLASHLIGHT] ?: false
    }

    suspend fun saveFlash(isFlashEnabled : Boolean) {
        dataStore.edit { preferences ->
            preferences[FLASHLIGHT] = isFlashEnabled
        }
    }

    // Vibrate
    var vibrate : Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[VIBRATE] ?: true
    }

    suspend fun saveVibrate(isVibrateEnabled : Boolean) {
        dataStore.edit { preferences ->
            preferences[VIBRATE] = isVibrateEnabled
        }
    }

    // Continuous Scanning
    var continuousScanning : Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[CONTINUOUS_SCANNING] ?: false
    }

    suspend fun saveContinuousScanning(isContinuousScanning : Boolean) {
        dataStore.edit { preferences ->
            preferences[CONTINUOUS_SCANNING] = isContinuousScanning
        }
    }

    // Confirm Scans Manually
    var confirmScansManually : Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[CONFIRM_SCANS_MANUALLY] ?: false
    }

    suspend fun saveConfirmScansManually(isConfirmScansManually : Boolean) {
        dataStore.edit { preferences ->
            preferences[CONFIRM_SCANS_MANUALLY] = isConfirmScansManually
        }
    }

    // Is Back Camera
    var isBackCamera : Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[IS_BACK_CAMERA] ?: true
    }

    suspend fun saveIsBackCamera(isBackCamera : Boolean) {
        dataStore.edit { preferences ->
            preferences[IS_BACK_CAMERA] = isBackCamera
        }
    }

    // Save Scanned Barcodes to History
    var saveScannedBarcodesToHistory : Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[SAVE_SCANNED_BARCODES_TO_HISTORY] ?: true
    }

    suspend fun saveSaveScannedBarcodesToHistory(isSaveScannedBarcodesToHistory : Boolean) {
        dataStore.edit { preferences ->
            preferences[SAVE_SCANNED_BARCODES_TO_HISTORY] = isSaveScannedBarcodesToHistory
        }
    }

    // Save Created Barcodes to History
    var saveCreatedBarcodesToHistory : Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[SAVE_CREATED_BARCODES_TO_HISTORY] ?: true
    }

    suspend fun saveSaveCreatedBarcodesToHistory(isSaveCreatedBarcodesToHistory : Boolean) {
        dataStore.edit { preferences ->
            preferences[SAVE_CREATED_BARCODES_TO_HISTORY] = isSaveCreatedBarcodesToHistory
        }
    }

    // Do Not Save Duplicates
    var doNotSaveDuplicates : Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[DO_NOT_SAVE_DUPLICATES] ?: false
    }

    suspend fun saveDoNotSaveDuplicates(isDoNotSaveDuplicates : Boolean) {
        dataStore.edit { preferences ->
            preferences[DO_NOT_SAVE_DUPLICATES] = isDoNotSaveDuplicates
        }
    }

    // Search Engine (assuming SearchEngine is Parcelable or Serializable)
    var searchEngine : Flow<SearchEngine> = dataStore.data.map { preferences ->
        SearchEngine.valueOf(preferences[SEARCH_ENGINE] ?: SearchEngine.NONE.name)
    }

    suspend fun saveSearchEngine(searchEngine : SearchEngine) {
        dataStore.edit { preferences ->
            preferences[SEARCH_ENGINE] = searchEngine.name
        }
    }


    // Format Selection
    fun isFormatSelected(format : BarcodeFormat) : Flow<Boolean> {
        return dataStore.data.map { preferences ->
            preferences[booleanPreferencesKey(format.name)] ?: true
        }
    }

    suspend fun setFormatSelected(format : BarcodeFormat , isSelected : Boolean) {
        dataStore.edit { preferences ->
            preferences[booleanPreferencesKey(format.name)] = isSelected
        }
    }
}
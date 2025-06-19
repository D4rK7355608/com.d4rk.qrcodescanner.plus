package com.d4rk.android.libs.apptoolkit.app.theme.style

import android.app.Activity
import android.content.Context
import android.os.Build
import android.os.PowerManager
import android.view.View
import android.view.Window
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.d4rk.android.libs.apptoolkit.core.utils.constants.datastore.DataStoreNamesConstants
import com.d4rk.android.libs.apptoolkit.data.datastore.CommonDataStore

private val defaultLightScheme : ColorScheme = lightColorScheme()
private val defaultDarkScheme : ColorScheme = darkColorScheme()

object AppThemeConfig {
    var customLightScheme : ColorScheme? = null
    var customDarkScheme : ColorScheme? = null
}

private fun getColorScheme(isDarkTheme : Boolean , isAmoledMode : Boolean , isDynamicColors : Boolean , context : Context) : ColorScheme {
    val powerManager = context.getSystemService(Context.POWER_SERVICE) as PowerManager
    val isBatterySaverOn = powerManager.isPowerSaveMode

    val baseLightScheme = AppThemeConfig.customLightScheme ?: defaultLightScheme
    val baseDarkScheme = AppThemeConfig.customDarkScheme ?: defaultDarkScheme


    val dynamicDark : ColorScheme = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) dynamicDarkColorScheme(context) else baseDarkScheme
    val dynamicLight : ColorScheme = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) dynamicLightColorScheme(context) else baseLightScheme

    val shouldUseDarkTheme = isDarkTheme || isBatterySaverOn

    return when {
        isAmoledMode && shouldUseDarkTheme && isDynamicColors -> dynamicDark.copy(
            surface = Color.Black ,
            background = Color.Black ,
        )

        isAmoledMode && shouldUseDarkTheme -> baseDarkScheme.copy(
            surface = Color.Black ,
            background = Color.Black ,
        )

        isDynamicColors -> if (shouldUseDarkTheme) dynamicDark else dynamicLight
        else -> if (shouldUseDarkTheme) baseDarkScheme else baseLightScheme
    }
}

@Composable
fun AppTheme(content : @Composable () -> Unit) {
    val context : Context = LocalContext.current
    val dataStore : CommonDataStore = CommonDataStore.getInstance(context = context)
    val themeMode : String = dataStore.themeMode.collectAsState(initial = DataStoreNamesConstants.THEME_MODE_FOLLOW_SYSTEM).value
    val isDynamicColors : Boolean = dataStore.dynamicColors.collectAsState(initial = true).value
    val isAmoledMode : Boolean = dataStore.amoledMode.collectAsState(initial = false).value

    val isSystemDarkTheme : Boolean = isSystemInDarkTheme()
    val isDarkTheme : Boolean = when (themeMode) {
        DataStoreNamesConstants.THEME_MODE_DARK -> true
        DataStoreNamesConstants.THEME_MODE_LIGHT -> false
        else -> isSystemDarkTheme
    }

    val colorScheme : ColorScheme = getColorScheme(isDarkTheme , isAmoledMode , isDynamicColors , context)

    val view : View = LocalView.current
    if (! view.isInEditMode) {
        SideEffect {
            val window : Window = (view.context as Activity).window
            @Suppress("DEPRECATION")
            window.statusBarColor = Color.Transparent.toArgb()
            WindowCompat.getInsetsController(window , view).isAppearanceLightStatusBars = ! isDarkTheme
        }
    }

    MaterialTheme(colorScheme = colorScheme , content = content)
}
package com.d4rk.android.libs.apptoolkit.app.oboarding.ui.components.pages

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BrightnessAuto
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.d4rk.android.libs.apptoolkit.R
import com.d4rk.android.libs.apptoolkit.app.oboarding.domain.data.model.OnboardingThemeChoice
import com.d4rk.android.libs.apptoolkit.app.oboarding.ui.components.AmoledModeToggle
import com.d4rk.android.libs.apptoolkit.app.oboarding.ui.components.ThemeChoiceCard
import com.d4rk.android.libs.apptoolkit.core.ui.components.spacers.ExtraExtraLargeVerticalSpacer
import com.d4rk.android.libs.apptoolkit.core.ui.components.spacers.LargeVerticalSpacer
import com.d4rk.android.libs.apptoolkit.core.utils.constants.datastore.DataStoreNamesConstants
import com.d4rk.android.libs.apptoolkit.core.utils.constants.ui.SizeConstants
import com.d4rk.android.libs.apptoolkit.data.datastore.CommonDataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun ThemeOnboardingPageTab() {
    val coroutineScope: CoroutineScope = rememberCoroutineScope()
    val context: Context = LocalContext.current
    val dataStore: CommonDataStore = CommonDataStore.getInstance(context = context)

    val defaultThemeModeName: String = stringResource(id = R.string.follow_system)
    val currentThemeMode: String by dataStore.themeMode.collectAsState<String, String>(initial = defaultThemeModeName)
    val isAmoledMode: Boolean by dataStore.amoledMode.collectAsState<Boolean, Boolean>(initial = false)

    val themeChoices: List<OnboardingThemeChoice> = listOf(
        OnboardingThemeChoice(
            key = DataStoreNamesConstants.THEME_MODE_LIGHT,
            displayName = stringResource(id = R.string.light_mode),
            icon = Icons.Filled.LightMode,
            description = stringResource(R.string.onboarding_theme_light_desc)
        ), OnboardingThemeChoice(
            key = DataStoreNamesConstants.THEME_MODE_DARK,
            displayName = stringResource(id = R.string.dark_mode),
            icon = Icons.Filled.DarkMode,
            description = stringResource(R.string.onboarding_theme_dark_desc)
        ), OnboardingThemeChoice(
            key = DataStoreNamesConstants.THEME_MODE_FOLLOW_SYSTEM,
            displayName = stringResource(id = R.string.follow_system),
            icon = Icons.Filled.BrightnessAuto,
            description = stringResource(R.string.onboarding_theme_system_desc)
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(state = rememberScrollState())
            .padding(horizontal = SizeConstants.LargeSize),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.onboarding_theme_title),
            style = MaterialTheme.typography.headlineLarge.copy(
                fontWeight = FontWeight.Bold, fontSize = 30.sp, textAlign = TextAlign.Center
            ),
            color = MaterialTheme.colorScheme.onSurface
        )

        LargeVerticalSpacer()

        Text(
            text = stringResource(R.string.onboarding_theme_subtitle),
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(horizontal = SizeConstants.LargeSize)
        )

        themeChoices.forEachIndexed { index, choice ->
            ThemeChoiceCard(
                choice = choice, isSelected = currentThemeMode == choice.key, onClick = {
                    coroutineScope.launch {
                        dataStore.saveThemeMode(mode = choice.key)

                    }
                })
            if (index < themeChoices.lastIndex) {
                LargeVerticalSpacer()
            }
        }

        LargeVerticalSpacer()

        HorizontalDivider(
            modifier = Modifier.padding(vertical = SizeConstants.LargeSize),
            thickness = SizeConstants.ExtraTinySize / 4
        )

        LargeVerticalSpacer()

        AmoledModeToggle(
            isAmoledMode = isAmoledMode, onCheckedChange = { isChecked ->
                coroutineScope.launch {
                    dataStore.saveAmoledMode(isChecked = isChecked)
                }
            })

        ExtraExtraLargeVerticalSpacer()
    }
}
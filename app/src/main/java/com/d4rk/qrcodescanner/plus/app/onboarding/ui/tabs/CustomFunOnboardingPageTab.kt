package com.d4rk.qrcodescanner.plus.app.onboarding.ui.tabs

import android.content.Intent
import android.view.SoundEffectConstants
import android.view.View
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import com.d4rk.qrcodescanner.plus.app.main.ui.MainActivity
import com.d4rk.android.libs.apptoolkit.core.ui.components.modifiers.bounceClick
import com.d4rk.android.libs.apptoolkit.core.ui.components.spacers.MediumVerticalSpacer
import com.d4rk.android.libs.apptoolkit.core.utils.constants.ui.SizeConstants

@Composable
fun CustomFunOnboardingPageTab() {
    val context = LocalContext.current
    val view : View = LocalView.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(SizeConstants.ExtraLargeIncreasedSize),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("This is a fully custom tab!")
        MediumVerticalSpacer()
        Button(onClick = {
            view.playSoundEffect(SoundEffectConstants.CLICK)
            context.startActivity(Intent(context, MainActivity::class.java))
        } , modifier = Modifier.bounceClick()) {
            Text("Let's go!")
        }
    }
}
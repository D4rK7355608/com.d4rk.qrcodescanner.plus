package com.d4rk.android.libs.apptoolkit.app.oboarding.ui.components.pages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.d4rk.android.libs.apptoolkit.app.oboarding.domain.data.model.ui.OnboardingPage
import com.d4rk.android.libs.apptoolkit.app.oboarding.ui.components.AnimatedMorphingShapeContainer
import com.d4rk.android.libs.apptoolkit.core.ui.components.spacers.LargeVerticalSpacer
import com.d4rk.android.libs.apptoolkit.core.utils.constants.ui.SizeConstants

@Composable
fun OnboardingDefaultPageLayout(page: OnboardingPage.DefaultPage) {
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = SizeConstants.LargeSize)
                .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        AnimatedMorphingShapeContainer(page.imageVector)

        Text(
            text = page.title,
            style = MaterialTheme.typography.headlineLarge.copy(fontSize = 32.sp),
            textAlign = TextAlign.Center
        )
        LargeVerticalSpacer()
        Text(
            text = page.description,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = SizeConstants.ExtraLargeIncreasedSize)
        )
    }
}
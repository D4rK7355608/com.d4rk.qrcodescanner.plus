package com.d4rk.android.libs.apptoolkit.app.help.ui

import android.app.Activity
import android.content.Context
import android.view.SoundEffectConstants
import android.view.View
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.RateReview
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import com.d4rk.android.libs.apptoolkit.R
import com.d4rk.android.libs.apptoolkit.app.help.domain.data.model.HelpScreenConfig
import com.d4rk.android.libs.apptoolkit.app.help.domain.data.model.UiHelpQuestion
import com.d4rk.android.libs.apptoolkit.app.help.ui.components.ContactUsCard
import com.d4rk.android.libs.apptoolkit.app.help.ui.components.HelpQuestionsList
import com.d4rk.android.libs.apptoolkit.app.help.ui.components.dropdown.HelpScreenMenuActions
import com.d4rk.android.libs.apptoolkit.core.ui.components.buttons.fab.AnimatedExtendedFloatingActionButton
import com.d4rk.android.libs.apptoolkit.core.ui.components.navigation.LargeTopAppBarWithScaffold
import com.d4rk.android.libs.apptoolkit.core.ui.components.spacers.MediumVerticalSpacer
import com.d4rk.android.libs.apptoolkit.core.utils.constants.ui.SizeConstants
import com.d4rk.android.libs.apptoolkit.core.utils.helpers.IntentsHelper
import com.d4rk.android.libs.apptoolkit.core.utils.helpers.ReviewHelper

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HelpScreen(activity : Activity , config : HelpScreenConfig) {
    val scrollBehavior : TopAppBarScrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(state = rememberTopAppBarState())
    val context : Context = LocalContext.current
    val view : View = LocalView.current
    val isFabExtended : MutableState<Boolean> = remember { mutableStateOf(value = true) }
    val faqList: List<UiHelpQuestion> = rememberFaqList()

    LaunchedEffect(key1 = scrollBehavior.state.contentOffset) {
        isFabExtended.value = scrollBehavior.state.contentOffset >= 0f
    }

    LargeTopAppBarWithScaffold(title = stringResource(id = R.string.help) , onBackClicked = { activity.finish() } , actions = {
        HelpScreenMenuActions(context = context , activity = activity , showDialog = remember { mutableStateOf(value = false) } , view = view , config = config)
    } , scrollBehavior = scrollBehavior , floatingActionButton = {
        AnimatedExtendedFloatingActionButton(visible = true , expanded = isFabExtended.value , onClick = {
            ReviewHelper.forceLaunchInAppReview(activity = activity)
        } , text = { Text(text = stringResource(id = R.string.feedback)) } , icon = { Icon(Icons.Outlined.RateReview , contentDescription = null) })
    }) { paddingValues ->
        HelpScreenContent(questions = faqList , paddingValues = paddingValues , activity = activity , view = view)
    }
}

@Composable
fun HelpScreenContent(questions : List<UiHelpQuestion> , paddingValues : PaddingValues , activity : Activity , view : View) {
    LazyColumn(
        modifier = Modifier.fillMaxSize() , contentPadding = PaddingValues(
            top = paddingValues.calculateTopPadding() , bottom = paddingValues.calculateBottomPadding() , start = SizeConstants.LargeSize , end = SizeConstants.LargeSize
        )
    ) {
        item {
            Text(text = stringResource(id = R.string.popular_help_resources))
            MediumVerticalSpacer()
            Card(modifier = Modifier.fillMaxWidth()) {
                HelpQuestionsList(questions = questions)
            }
            MediumVerticalSpacer()
            ContactUsCard(onClick = {
                view.playSoundEffect(SoundEffectConstants.CLICK)
                IntentsHelper.sendEmailToDeveloper(context = activity , applicationNameRes = R.string.app_name)
            })
            Spacer(modifier = Modifier.height(height = SizeConstants.ExtraExtraLargeSize * 2))
        }
    }
}
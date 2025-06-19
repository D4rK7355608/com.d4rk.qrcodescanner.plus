package com.d4rk.android.libs.apptoolkit.core.ui.components.dialogs

import android.view.SoundEffectConstants
import android.view.View
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.d4rk.android.libs.apptoolkit.core.ui.components.modifiers.bounceClick
import com.d4rk.android.libs.apptoolkit.core.utils.constants.ui.SizeConstants

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BasicFullScreenDialog(title : String , onDismiss : () -> Unit , onConfirm : () -> Unit , confirmEnabled : Boolean = true , confirmButtonText : String = stringResource(id = android.R.string.ok) , content : @Composable ColumnScope.() -> Unit) {
    val view : View = LocalView.current

    Dialog(onDismissRequest = onDismiss , properties = DialogProperties(dismissOnBackPress = true , dismissOnClickOutside = true , usePlatformDefaultWidth = false , decorFitsSystemWindows = true)) {
        Scaffold(
            modifier = Modifier.fillMaxSize() , topBar = {
                CenterAlignedTopAppBar(navigationIcon = {
                    IconButton(modifier = Modifier.bounceClick() , onClick = onDismiss) {
                        Icon(imageVector = Icons.Filled.Close , contentDescription = null)
                    }
                } , title = { Text(text = title) } , actions = {
                    TextButton(modifier = Modifier.bounceClick() , onClick = {
                        view.playSoundEffect(SoundEffectConstants.CLICK)
                        onConfirm()
                    } , enabled = confirmEnabled) {
                        Text(confirmButtonText)
                    }
                })
            }) { innerPadding : PaddingValues ->
            Column(
                modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues = innerPadding)
                        .padding(horizontal = SizeConstants.LargeSize , vertical = SizeConstants.SmallSize)
                        .verticalScroll(state = rememberScrollState()) , verticalArrangement = Arrangement.spacedBy(space = SizeConstants.MediumSize)
            ) {
                content()
            }
        }
    }
}
package com.d4rk.qrcodescanner.plus.ui.screens.settings.scanner.camera

import android.content.Context
import android.view.View
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import com.d4rk.qrcodescanner.plus.R
import com.d4rk.qrcodescanner.plus.data.datastore.DataStore
import com.d4rk.qrcodescanner.plus.utils.compose.components.RadioButtonPreferenceItem
import com.d4rk.qrcodescanner.plus.utils.haptic.weakHapticFeedback
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChooseCameraScreen(activity : _root_ide_package_.com.d4rk.qrcodescanner.plus.ui.screens.settings.scanner.camera.ChooseCameraActivity) {
    val context: Context = LocalContext.current
    val view : View = LocalView.current
    val scrollBehavior : TopAppBarScrollBehavior =
            TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    val dataStore: DataStore = DataStore.getInstance(context)
    val scope: CoroutineScope = rememberCoroutineScope()
    val isBackCamera : Boolean by dataStore.isBackCamera.collectAsState(initial = true)
    Scaffold(modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection) , topBar = {
        LargeTopAppBar(title = { Text(stringResource(R.string.camera)) } , navigationIcon = {
            IconButton(onClick = {
                view.weakHapticFeedback()
                activity.finish()
            }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack , contentDescription = null)
            }
        } , scrollBehavior = scrollBehavior)
    }) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                    .fillMaxHeight()
                    .padding(paddingValues) ,
        ) {
            item {
                RadioButtonPreferenceItem(
                    text = stringResource(R.string.back),
                    isChecked = isBackCamera
                ) { isChecked ->
                    scope.launch {
                        dataStore.saveIsBackCamera(isChecked)
                    }
                }

                RadioButtonPreferenceItem(
                    text = stringResource(R.string.front),
                    isChecked = !isBackCamera
                ) { isChecked ->
                    scope.launch {
                        dataStore.saveIsBackCamera(!isChecked)
                    }
                }
            }
        }
    }
}
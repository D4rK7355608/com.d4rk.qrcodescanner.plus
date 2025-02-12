package com.d4rk.qrcodescanner.plus.ui.screens.scan.screens.nocamerapermission

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun NoCameraPermissionScreen(
    onRequestPermission : () -> Unit
) {
    Box(
        modifier = Modifier
                .fillMaxSize()
                .padding(all = 16.dp) , contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally ,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Outlined.CameraAlt ,
                contentDescription = null ,
                modifier = Modifier
                        .size(size = 120.dp)
                        .padding(bottom = 16.dp)
            )

            Text(
                text = "Camera Permission Required" ,
                modifier = Modifier.padding(bottom = 16.dp) ,
            )

            Button(onClick = onRequestPermission) {
                Text(text = "Grant Permission")
            }
        }
    }
}
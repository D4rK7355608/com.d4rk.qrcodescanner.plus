package com.d4rk.cleaner.data.model.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.outlined.QrCodeScanner
import androidx.compose.material.icons.rounded.Create
import androidx.compose.material.icons.rounded.History
import androidx.compose.material.icons.rounded.QrCode
import androidx.compose.material.icons.sharp.Create
import androidx.compose.material.icons.sharp.History
import androidx.compose.material.icons.sharp.QrCode
import androidx.compose.ui.graphics.vector.ImageVector
import com.d4rk.qrcodescanner.plus.R
import com.d4rk.qrcodescanner.plus.utils.constants.ui.bottombar.BottomBarRoutes

sealed class BottomNavigationScreen(
    val route: String, val icon: ImageVector, val selectedIcon: ImageVector, val title: Int
) {
    data object Scan : BottomNavigationScreen(
        BottomBarRoutes.SCAN , Icons.Outlined.QrCodeScanner , Icons.Filled.QrCodeScanner , R.string.scan
    )

    data object Create : BottomNavigationScreen(
        BottomBarRoutes.CREATE ,
        Icons.Sharp.Create ,
        Icons.Rounded.Create ,
        R.string.create
    )

    data object History : BottomNavigationScreen(
        BottomBarRoutes.HISTORY ,
        Icons.Sharp.History ,
        Icons.Rounded.History ,
        R.string.history
    )
}
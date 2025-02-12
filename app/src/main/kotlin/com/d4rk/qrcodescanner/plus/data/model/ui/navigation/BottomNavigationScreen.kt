package com.d4rk.qrcodescanner.plus.data.model.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.QrCode
import androidx.compose.material.icons.outlined.QrCodeScanner
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.Create
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.History
import androidx.compose.material.icons.sharp.AutoAwesome
import androidx.compose.material.icons.sharp.Create
import androidx.compose.material.icons.sharp.FavoriteBorder
import androidx.compose.material.icons.sharp.History
import androidx.compose.ui.graphics.vector.ImageVector
import com.d4rk.qrcodescanner.plus.R
import com.d4rk.qrcodescanner.plus.utils.constants.ui.bottombar.BottomBarRoutes

sealed class BottomNavigationScreen(
    val route : String , val icon : ImageVector , val selectedIcon : ImageVector , val title : Int
) {
    data object Home : BottomNavigationScreen(
        route = BottomBarRoutes.SCAN ,
        icon = Icons.Outlined.QrCodeScanner ,
        selectedIcon = Icons.Filled.QrCodeScanner ,
        title = R.string.scan
    )

    data object StudioBot : BottomNavigationScreen(
        route = BottomBarRoutes.CREATE ,
        icon = Icons.Sharp.Create ,
        selectedIcon = Icons.Rounded.Create ,
        title = R.string.create
    )

    data object Favorites : BottomNavigationScreen(
        route = BottomBarRoutes.HISTORY ,
        icon = Icons.Sharp.History ,
        selectedIcon = Icons.Rounded.History ,
        title = R.string.history
    )
}
package com.d4rk.android.libs.apptoolkit.app.main.utils.interfaces

import androidx.compose.ui.graphics.vector.ImageVector

interface BottomNavigationItem {
    val route : String
    val icon : ImageVector
    val selectedIcon : ImageVector
    val title : Int
}
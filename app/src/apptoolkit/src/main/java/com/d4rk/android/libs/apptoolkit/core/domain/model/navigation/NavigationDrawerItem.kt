package com.d4rk.android.libs.apptoolkit.core.domain.model.navigation

import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Represents an item in a navigation drawer.
 *
 * @property title The resource ID of the string to display as the title of the item.
 * @property selectedIcon The icon to display when the item is selected.
 * @property badgeText An optional string to display as a badge on the item, defaults to an empty string.
 */
data class NavigationDrawerItem(
    val title : Int , val selectedIcon : ImageVector , val badgeText : String = ""
)
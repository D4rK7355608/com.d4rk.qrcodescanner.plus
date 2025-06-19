package com.d4rk.android.libs.apptoolkit.core.ui.components.preferences

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import com.d4rk.android.libs.apptoolkit.core.utils.constants.ui.SizeConstants

/**
 * Displays a category header within preference screens.
 *
 * This composable function renders a distinct header for preference categories, enhancing the visual organization of settings screens. It uses a primary color and semi-bold text styling to clearly distinguish the category title from individual preference items.
 *
 * @param title The text to be displayed as the category header. This should clearly and concisely name the preference category.
 */
@Composable
fun PreferenceCategoryItem(title : String) {
    Text(text = title , color = MaterialTheme.colorScheme.primary , style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold) , modifier = Modifier.padding(start = SizeConstants.LargeSize , top = SizeConstants.LargeSize) , maxLines = 1 , overflow = TextOverflow.Ellipsis)
}
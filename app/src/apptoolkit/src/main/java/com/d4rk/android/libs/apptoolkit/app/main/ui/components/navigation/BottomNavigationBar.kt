package com.d4rk.android.libs.apptoolkit.app.main.ui.components.navigation

import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import com.d4rk.android.libs.apptoolkit.app.main.utils.interfaces.BottomNavigationItem
import com.d4rk.android.libs.apptoolkit.core.ui.components.modifiers.bounceClick

@Composable
fun BottomNavigationBar(modifier : Modifier = Modifier, items : List<BottomNavigationItem> , currentRoute : String? , onItemSelected : (BottomNavigationItem) -> Unit , showLabels : Boolean = true) {
    Column(modifier = modifier) {
        NavigationBar {
            items.forEach { item ->
                NavigationBarItem(icon = {
                    Icon(
                        imageVector = if (currentRoute == item.route) item.selectedIcon else item.icon , contentDescription = null , modifier = Modifier.bounceClick()
                    )
                } , label = {
                    if (showLabels) {
                        Text(text = stringResource(id = item.title) , overflow = TextOverflow.Ellipsis , modifier = Modifier.basicMarquee())
                    }
                } , selected = currentRoute == item.route , onClick = { onItemSelected(item) })
            }
        }
    }
}
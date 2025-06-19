package com.d4rk.android.libs.apptoolkit.app.main.ui.components.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.d4rk.android.libs.apptoolkit.app.main.utils.interfaces.BottomNavigationItem
import com.d4rk.android.libs.apptoolkit.core.domain.model.navigation.NavigationDrawerItem
import com.d4rk.android.libs.apptoolkit.core.ui.components.modifiers.bounceClick

@Composable
fun LeftNavigationRail(
    modifier : Modifier = Modifier,
    bottomItems : List<BottomNavigationItem> = emptyList() ,
    drawerItems : List<NavigationDrawerItem> = emptyList() ,
    currentRoute : String? ,
    isRailExpanded : Boolean = false ,
    paddingValues : PaddingValues ,
    onBottomItemClick : (BottomNavigationItem) -> Unit = {} ,
    onDrawerItemClick : (NavigationDrawerItem) -> Unit = {} ,
    content : @Composable () -> Unit ,
) {
    val railWidth : Dp by animateDpAsState(targetValue = if (isRailExpanded) 200.dp else 72.dp , animationSpec = tween(durationMillis = 300))
    val textEntryAnimation : EnterTransition = fadeIn(animationSpec = tween(durationMillis = 300)) + expandHorizontally() + expandVertically()
    val textExitAnimation : ExitTransition = fadeOut(animationSpec = tween(durationMillis = 300)) + shrinkHorizontally() + shrinkVertically()

    Row(modifier = modifier.padding(top = paddingValues.calculateTopPadding())) {
        NavigationRail(modifier = Modifier.width(railWidth)) {
            bottomItems.forEach { item : BottomNavigationItem ->
                val isSelected : Boolean = currentRoute == item.route

                NavigationRailItem(modifier = Modifier.bounceClick() , selected = isSelected , onClick = { onBottomItemClick(item) } , icon = {
                    Icon(imageVector = if (isSelected) item.selectedIcon else item.icon , contentDescription = stringResource(item.title) , modifier = Modifier.bounceClick())
                } , label = {
                    AnimatedVisibility(visible = isRailExpanded , enter = textEntryAnimation , exit = textExitAnimation) {
                        Text(text = stringResource(id = item.title) , maxLines = 1 , overflow = TextOverflow.Ellipsis)
                    }
                })
            }
            Spacer(modifier = Modifier.weight(weight = 1f))
            drawerItems.forEach { item : NavigationDrawerItem ->
                NavigationRailItem(selected = false , onClick = { onDrawerItemClick(item) } , icon = {
                    Icon(imageVector = item.selectedIcon , contentDescription = stringResource(id = item.title) , modifier = Modifier.bounceClick())
                } , label = {
                    AnimatedVisibility(visible = isRailExpanded , enter = textEntryAnimation , exit = textExitAnimation) {
                        Text(text = stringResource(id = item.title))
                    }
                })
            }
        }

        Box(modifier = Modifier.weight(weight = 1f)) {
            content()
        }
    }
}
package com.d4rk.android.libs.apptoolkit.core.ui.components.navigation

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import com.d4rk.android.libs.apptoolkit.core.ui.components.buttons.AnimatedButtonDirection

/**
 * A composable function that creates a screen layout with a large top app bar and a scaffold.
 *
 * This function provides a convenient way to structure a screen with a collapsing large top app bar,
 * optional floating action button, and custom content area.
 *
 * @param title The title to display in the large top app bar.
 * @param onBackClicked The callback to be invoked when the back navigation icon is clicked.
 * @param actions The actions to display in the large top app bar's action area. This is a composable
 *                lambda that receives a [RowScope] to lay out the actions horizontally. Defaults to an
 *                empty lambda, meaning no actions.
 * @param floatingActionButton An optional composable lambda that creates a floating action button. If
 *                             null, no floating action button is displayed. Defaults to null.
 * @param scrollBehavior The [TopAppBarScrollBehavior] to control how the large top app bar collapses
 *                       and expands with scrolling. Defaults to `TopAppBarDefaults.exitUntilCollapsedScrollBehavior()`.
 * @param content The composable lambda for the main content of the screen. It receives [PaddingValues]
 *                representing the padding applied by the scaffold to accommodate the top app bar and
 *                floating action button.
 *
 * Example usage:
 * ```
 * LargeTopAppBarWithScaffold(
 *     title = "My Screen",
 *     onBackClicked = { navController.popBackStack() },
 *     actions = */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LargeTopAppBarWithScaffold(
    title : String ,
    onBackClicked : () -> Unit ,
    actions : @Composable (RowScope.() -> Unit) = {} ,
    floatingActionButton : @Composable (() -> Unit)? = null ,
    snackbarHostState : SnackbarHostState = SnackbarHostState() ,
    scrollBehavior : TopAppBarScrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior() ,
    content : @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection) ,
        topBar = {
            LargeTopAppBar(title = { Text(modifier = Modifier.animateContentSize() , text = title) } , navigationIcon = {
                AnimatedButtonDirection(icon = Icons.AutoMirrored.Filled.ArrowBack , contentDescription = stringResource(id = com.d4rk.android.libs.apptoolkit.R.string.go_back) , onClick = { onBackClicked() })
            } , actions = actions , scrollBehavior = scrollBehavior)
        } ,
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) } ,
        floatingActionButton = floatingActionButton ?: {} ,
    ) { paddingValues ->
        content(paddingValues)
    }
}

/**
 * A composable function that provides a scaffold with a large top app bar that supports scrolling behavior.
 *
 * This function simplifies the creation of a common UI pattern where a large top app bar is used
 * and the content scrolls under it, collapsing the app bar as the user scrolls down.
 *
 * @param title The text to be displayed in the large top app bar.
 * @param content The composable content to be displayed within the scaffold's body.
 *                This content will receive [PaddingValues] that account for the top app bar's height.
 *                Use this padding to correctly position your content.
 *
 * Example Usage:
 * ```
 * TopAppBarScaffold(title = "My App") { paddingValues ->
 *     LazyColumn(modifier = Modifier.padding(paddingValues)) {
 *         items(100) { index ->
 *             Text(text = "Item $index")
 *         }
 *     }
 * }
 * ```
 *
 * In this example, the `LazyColumn` is padded appropriately to ensure the content starts
 * below the LargeTopAppBar.
 *
 * @OptIn ExperimentalMaterial3Api is used because [LargeTopAppBar] and [TopAppBarScrollBehavior] are experimental.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBarScaffold(title : String , content : @Composable (PaddingValues) -> Unit , floatingActionButton : @Composable (() -> Unit)? = null) {
    val scrollBehaviorState : TopAppBarScrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(state = rememberTopAppBarState())

    Scaffold(
        modifier = Modifier.nestedScroll(connection = scrollBehaviorState.nestedScrollConnection) ,
        topBar = {
            LargeTopAppBar(title = { Text(modifier = Modifier.animateContentSize() , text = title) } , scrollBehavior = scrollBehaviorState)
        } ,
        floatingActionButton = floatingActionButton ?: {} ,
    ) { paddingValues ->
        content(paddingValues)
    }
}
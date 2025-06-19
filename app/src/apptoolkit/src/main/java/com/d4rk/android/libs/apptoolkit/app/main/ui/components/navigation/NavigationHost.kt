package com.d4rk.android.libs.apptoolkit.app.main.ui.components.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost

@Composable
fun NavigationHost(
    navController : NavHostController , startDestination : String , navGraphBuilder : NavGraphBuilder.() -> Unit
) {
    NavHost(
        navController = navController ,
        startDestination = startDestination ,
        enterTransition = NavigationTransitions.DefaultEnter ,
        exitTransition = NavigationTransitions.DefaultExit ,
        popEnterTransition = NavigationTransitions.DefaultEnter ,
        popExitTransition = NavigationTransitions.DefaultExit ,
        builder = navGraphBuilder
    )
}

object NavigationTransitions {

    private val fadeScaleEnterSpec : TweenSpec<Float> = tween<Float>(durationMillis = 200)
    private val fadeScaleExitSpec : TweenSpec<Float> = tween<Float>(durationMillis = 200)

    val DefaultEnter : AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition by lazy {
        {
            fadeIn(animationSpec = fadeScaleEnterSpec) + scaleIn(initialScale = 0.92f , animationSpec = fadeScaleEnterSpec)
        }
    }

    val DefaultExit : AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition by lazy {
        {
            fadeOut(animationSpec = fadeScaleExitSpec) + scaleOut(targetScale = 0.95f , animationSpec = fadeScaleExitSpec)
        }
    }
}
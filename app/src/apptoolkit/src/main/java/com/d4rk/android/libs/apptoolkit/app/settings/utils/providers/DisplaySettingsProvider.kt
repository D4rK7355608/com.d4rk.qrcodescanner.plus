package com.d4rk.android.libs.apptoolkit.app.settings.utils.providers

import androidx.compose.runtime.Composable

/**
 * Interface for providing access to display-related settings and UI components.
 *
 * This interface defines methods and properties for interacting with display settings,
 * such as managing the startup page, selecting the language, and opening the theme settings.
 */
interface DisplaySettingsProvider {

    /**
     * Indicates whether this component supports a startup page.
     *
     * A startup page is a designated page that is displayed to the user when the component is first launched or accessed.
     * If `true`, the component can display a specific page on startup. If `false`, the component does not have a dedicated
     * startup page and might default to a different initial view.
     *
     * @return `true` if the component supports a startup page, `false` otherwise.
     */
    val supportsStartupPage : Boolean
        get() = false

    /**
     * Displays a dialog that allows the user to select a startup option.
     *
     * This dialog presents the user with a choice of different startup modes or configurations.
     * Upon the user selecting an option, the dialog will dismiss and call the `onStartupSelected` callback.
     * If the user dismisses the dialog without making a selection, the `onDismiss` callback will be triggered.
     *
     * @param onDismiss Callback invoked when the dialog is dismissed without a selection being made.
     *                  This typically occurs when the user taps outside the dialog or presses the back button.
     * @param onStartupSelected Callback invoked when a startup option is selected by the user.
     *                          It provides the selected option as a `String`.
     *                          The specific format and meaning of this string are defined by the caller.
     * @param onDismiss default empty function
     */
    @Composable
    fun StartupPageDialog(
        onDismiss : () -> Unit , onStartupSelected : (String) -> Unit
    ) {

    }

    /**
     * Opens the theme settings screen or activity.
     *
     * This function is responsible for initiating the navigation or display of
     * the user interface where the user can customize the application's theme.
     * This might include options for:
     * - Switching between light and dark mode.
     * - Selecting a custom color scheme.
     * - Adjusting font sizes or styles.
     * - Enabling high contrast mode.
     *
     * The exact implementation details of how the theme settings are opened
     * (e.g., launching a new activity, displaying a dialog, etc.) are
     * handled internally by this function.
     *
     * @see [closeThemeSettings] for closing the theme settings
     */
    fun openThemeSettings() {

    }
}
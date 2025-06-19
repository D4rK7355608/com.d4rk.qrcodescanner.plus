package com.d4rk.android.libs.apptoolkit.app.issuereporter.domain.model.ui

/**
 * UI state holder for the Issue Reporter screen.
 */
data class UiIssueReporterScreen(
    val title: String = "",
    val description: String = "",
    val email: String = "",
    val anonymous: Boolean = true,
    val issueUrl: String? = null
)
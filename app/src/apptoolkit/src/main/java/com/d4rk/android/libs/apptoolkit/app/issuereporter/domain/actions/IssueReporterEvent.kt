package com.d4rk.android.libs.apptoolkit.app.issuereporter.domain.actions

import android.content.Context
import com.d4rk.android.libs.apptoolkit.core.ui.base.handling.UiEvent

sealed interface IssueReporterEvent : UiEvent {
    data class UpdateTitle(val value: String) : IssueReporterEvent
    data class UpdateDescription(val value: String) : IssueReporterEvent
    data class UpdateEmail(val value: String) : IssueReporterEvent
    data class SetAnonymous(val anonymous: Boolean) : IssueReporterEvent
    data class Send(val context: Context) : IssueReporterEvent
    data object DismissSnackbar : IssueReporterEvent
}
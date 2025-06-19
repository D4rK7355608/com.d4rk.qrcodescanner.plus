package com.d4rk.android.libs.apptoolkit.app.issuereporter.domain.model

import io.ktor.http.HttpStatusCode

sealed interface IssueReportResult {
    data class Success(val url: String) : IssueReportResult
    data class Error(val status: HttpStatusCode, val message: String) : IssueReportResult
}

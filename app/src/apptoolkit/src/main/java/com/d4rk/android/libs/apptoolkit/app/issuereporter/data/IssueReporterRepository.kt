package com.d4rk.android.libs.apptoolkit.app.issuereporter.data

import com.d4rk.android.libs.apptoolkit.app.issuereporter.domain.model.CreateIssueRequest
import com.d4rk.android.libs.apptoolkit.app.issuereporter.domain.model.IssueReportResult
import com.d4rk.android.libs.apptoolkit.app.issuereporter.domain.model.Report
import com.d4rk.android.libs.apptoolkit.app.issuereporter.domain.model.github.GithubTarget
import io.ktor.client.HttpClient
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

class IssueReporterRepository(private val client : HttpClient) {

    suspend fun sendReport(
        report: Report,
        target: GithubTarget,
        token: String? = null
    ): IssueReportResult {
        val url = "https://api.github.com/repos/${target.username}/${target.repository}/issues"
        val response: HttpResponse = client.post(url) {
            contentType(ContentType.Application.Json)
            header("Accept", "application/vnd.github+json")
            token?.let { header("Authorization", "Bearer $it") }
            val issueRequest = CreateIssueRequest(
                title = report.title,
                body = report.getDescription(),
                labels = listOf("bug", "from-mobile")
            )
            setBody(Json.encodeToString(CreateIssueRequest.serializer(), issueRequest))
        }

        val responseBody = response.bodyAsText()
        println("GitHub response: $responseBody")

        return if (response.status == HttpStatusCode.Created) {
            val json = Json.parseToJsonElement(responseBody).jsonObject
            val issueUrl = json["html_url"]?.jsonPrimitive?.content ?: ""
            IssueReportResult.Success(issueUrl)
        } else {
            IssueReportResult.Error(response.status, responseBody)
        }
    }
}

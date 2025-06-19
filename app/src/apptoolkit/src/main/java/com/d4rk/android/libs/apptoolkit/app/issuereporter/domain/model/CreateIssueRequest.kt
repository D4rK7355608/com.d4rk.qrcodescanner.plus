package com.d4rk.android.libs.apptoolkit.app.issuereporter.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateIssueRequest(
    val title: String,
    val body: String,
    val labels: List<String>? = null,
    @SerialName("assignees") val assignees: List<String>? = null
)

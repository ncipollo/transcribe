package io.ncipollo.transcribe.api.atlassian

import kotlinx.serialization.Serializable

@Serializable
data class PageUpdateRequest(
    val id: String,
    val status: String,
    val title: String,
    val version: PageUpdateVersion,
    val body: PageUpdateBody,
)

@Serializable
data class PageUpdateVersion(
    val number: Int,
    val message: String? = null,
)

@Serializable
data class PageUpdateBody(
    val representation: String = "atlas_doc_format",
    val value: String,
)

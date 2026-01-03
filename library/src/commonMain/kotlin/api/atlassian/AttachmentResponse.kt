package api.atlassian

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AttachmentsResponse(
    val results: List<Attachment>,
    @SerialName("_links") val links: AttachmentsLinks? = null,
)

@Serializable
data class Attachment(
    val id: String,
    val status: String,
    val title: String,
    val createdAt: String,
    val pageId: String? = null,
    val blogPostId: String? = null,
    val customContentId: String? = null,
    val mediaType: String,
    val mediaTypeDescription: String? = null,
    val comment: String? = null,
    val fileId: String? = null,
    val fileSize: Long,
    val webuiLink: String? = null,
    val downloadLink: String? = null,
    val version: AttachmentVersion? = null,
    @SerialName("_links") val links: AttachmentLinks? = null,
)

@Serializable
data class AttachmentVersion(
    val createdAt: String,
    val message: String? = null,
    val number: Int,
    val minorEdit: Boolean,
    val authorId: String,
)

@Serializable
data class AttachmentLinks(
    val webui: String? = null,
    val download: String? = null,
)

@Serializable
data class AttachmentsLinks(
    val next: String? = null,
    val base: String? = null,
)

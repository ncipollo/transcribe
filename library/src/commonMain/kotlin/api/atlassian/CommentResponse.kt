package api.atlassian

import data.atlassian.adf.DocNode
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FooterCommentsResponse(
    val results: List<FooterComment>,
    @SerialName("_links") val links: CommentsLinks? = null,
)

@Serializable
data class FooterComment(
    val id: String,
    val status: String,
    val title: String? = null,
    val pageId: String? = null,
    val blogPostId: String? = null,
    val parentCommentId: String? = null,
    val version: CommentVersion,
    val body: CommentBody,
    @SerialName("_links") val links: CommentLinks? = null,
)

@Serializable
data class InlineCommentsResponse(
    val results: List<InlineComment>,
    @SerialName("_links") val links: CommentsLinks? = null,
)

@Serializable
data class InlineComment(
    val id: String,
    val status: String,
    val title: String? = null,
    val pageId: String? = null,
    val blogPostId: String? = null,
    val version: CommentVersion,
    val body: CommentBody,
    val resolutionStatus: String? = null,
    @SerialName("_links") val links: CommentLinks? = null,
)

@Serializable
data class CommentVersion(
    val createdAt: String,
    val message: String? = null,
    val number: Int,
    val minorEdit: Boolean,
    val authorId: String,
)

@Serializable
data class CommentBody(
    @SerialName("atlas_doc_format") val atlasDocFormat: CommentBodyADF,
)

@Serializable
data class CommentBodyADF(
    val value: String,
)

@Serializable
data class CommentLinks(
    val webui: String? = null,
)

@Serializable
data class CommentsLinks(
    val next: String? = null,
    val base: String? = null,
)

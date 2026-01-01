package api.atlassian

import data.atlassian.adf.ADFSerializer
import data.atlassian.adf.DocNode
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PageResponse(
    val id: String,
    val status: String,
    val title: String,
    val spaceId: String,
    val parentId: String? = null,
    val parentType: String? = null,
    val position: Int? = null,
    val authorId: String,
    val ownerId: String? = null,
    val createdAt: String,
    val version: PageVersion,
    val body: PageBody? = null,
    @SerialName("_links") val links: PageLinks? = null,
)

@Serializable
data class PageVersion(
    val createdAt: String,
    val message: String? = null,
    val number: Int,
    val minorEdit: Boolean,
    val authorId: String,
)

@Serializable
data class PageBody(
    @SerialName("atlas_doc_format") val atlasDocFormat: AtlasDocFormat? = null,
)

/**
 * Represents the atlas_doc_format body content from Confluence API.
 * The API returns the ADF document as a stringified JSON in the [value] field.
 * Use [docNode] to get the parsed ADF document.
 */
@Serializable
data class AtlasDocFormat(
    val value: String,
) {
    /**
     * Parses the stringified ADF JSON into a [DocNode].
     */
    val docNode: DocNode
        get() = ADFSerializer.fromJson(value)
}

@Serializable
data class PageLinks(
    val webui: String? = null,
    val editui: String? = null,
    val tinyui: String? = null,
)

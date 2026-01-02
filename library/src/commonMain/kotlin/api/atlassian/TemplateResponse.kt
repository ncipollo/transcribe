package api.atlassian

import data.atlassian.adf.ADFSerializer
import data.atlassian.adf.DocNode
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TemplateResponse(
    val templateId: String,
    val name: String,
    val description: String? = null,
    val templateType: String,
    val editorVersion: String? = null,
    val body: TemplateBody? = null,
    val space: TemplateSpace? = null,
    val labels: List<TemplateLabel>? = null,
    val originalTemplate: OriginalTemplate? = null,
    val referencingBlueprint: String? = null,
    @SerialName("_expandable") val expandable: TemplateExpandable? = null,
    @SerialName("_links") val links: TemplateLinks? = null,
)

@Serializable
data class TemplateBody(
    @SerialName("atlas_doc_format") val atlasDocFormat: TemplateAtlasDocFormat? = null,
)

/**
 * Represents the atlas_doc_format body content from Confluence Template API.
 * The API returns the ADF document as a stringified JSON in the [value] field.
 * Use [docNode] to get the parsed ADF document.
 */
@Serializable
data class TemplateAtlasDocFormat(
    val value: String,
) {
    /**
     * Parses the stringified ADF JSON into a [DocNode].
     */
    val docNode: DocNode
        get() = ADFSerializer.fromJson(value)
}

@Serializable
data class TemplateSpace(
    val key: String? = null,
    val id: String? = null,
)

@Serializable
data class TemplateLabel(
    val prefix: String? = null,
    val name: String,
    val id: String? = null,
    val label: String? = null,
)

@Serializable
data class OriginalTemplate(
    val pluginKey: String? = null,
    val moduleKey: String? = null,
)

@Serializable
data class TemplateExpandable(
    val body: String? = null,
)

@Serializable
data class TemplateLinks(
    val webui: String? = null,
    val editui: String? = null,
)

@Serializable
data class TemplateUpdateRequest(
    val templateId: String,
    val name: String,
    val templateType: String,
    val body: TemplateUpdateBody,
    val description: String? = null,
    val labels: List<TemplateLabel>? = null,
    val space: TemplateSpace? = null,
)

@Serializable
data class TemplateUpdateBody(
    @SerialName("atlas_doc_format") val atlasDocFormat: TemplateBodyValue? = null,
)

@Serializable
data class TemplateBodyValue(
    val value: String,
    val representation: String = "atlas_doc_format",
)

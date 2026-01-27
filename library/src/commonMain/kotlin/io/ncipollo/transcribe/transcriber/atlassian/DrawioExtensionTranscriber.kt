package io.ncipollo.transcribe.transcriber.atlassian

import io.ncipollo.transcribe.context.ADFTranscriberContext
import io.ncipollo.transcribe.data.atlassian.adf.ExtensionNode
import io.ncipollo.transcribe.files.toSnakeCase
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import io.ncipollo.transcribe.transcriber.TranscribeResult
import io.ncipollo.transcribe.transcriber.action.AttachmentDownload

/**
 * Transcriber for Draw.io extension nodes.
 * Converts Draw.io extensions to markdown image links.
 */
class DrawioExtensionTranscriber : ADFTranscriber<ExtensionNode> {
    override fun transcribe(
        input: ExtensionNode,
        context: ADFTranscriberContext,
    ): TranscribeResult<String> {
        val altText = extractAltText(input.attrs.parameters)
        val diagramName = extractDiagramName(input.attrs.parameters) ?: return TranscribeResult("")
        val downloadUrl = extractDownloadUrl(input.attrs.parameters) ?: return TranscribeResult("")

        val imagePath = imagePath(context, diagramName)
        val action = downloadAction(imagePath, downloadUrl)
        return TranscribeResult("![$altText]($imagePath)\n", listOf(action))
    }

    private fun extractAltText(parameters: JsonObject?): String {
        if (parameters == null) return ""

        val macroParams = parameters["macroParams"]?.jsonObject ?: return ""
        val diagramDisplayName = macroParams["diagramDisplayName"]?.jsonObject ?: return ""
        val displayName = diagramDisplayName["value"]?.jsonPrimitive?.content ?: return ""

        return displayName
    }

    private fun extractDiagramName(parameters: JsonObject?): String? {
        if (parameters == null) return null

        val macroParams = parameters["macroParams"]?.jsonObject ?: return null
        val diagramName = macroParams["diagramName"]?.jsonObject ?: return null

        return diagramName["value"]?.jsonPrimitive?.content
    }

    private fun extractDownloadUrl(parameters: JsonObject?): String? {
        if (parameters == null) return null

        val macroMetadata = parameters["macroMetadata"]?.jsonObject ?: return null
        val placeholder = macroMetadata["placeholder"]?.jsonArray ?: return null
        if (placeholder.isEmpty()) return null

        val firstPlaceholder = placeholder[0] as? JsonObject ?: return null
        val data = firstPlaceholder["data"]?.jsonObject ?: return null
        return data["url"]?.jsonPrimitive?.content
    }

    private fun imagePath(context: ADFTranscriberContext, diagramName: String): String {
        val folder = context.suggestedImageFolder
        val path = "$folder/${diagramName.toSnakeCase()}"
        return if (path.endsWith(".png")) path else "$path.png"
    }

    private fun downloadAction(imagePath: String, downloadUrl: String): AttachmentDownload {
        return AttachmentDownload(downloadPath = downloadUrl, localRelativePath = imagePath)
    }
}

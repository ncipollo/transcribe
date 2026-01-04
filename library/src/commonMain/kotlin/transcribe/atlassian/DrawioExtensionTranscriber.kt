package transcribe.atlassian

import context.ADFTranscriberContext
import data.atlassian.adf.ExtensionNode
import files.toSnakeCase
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import transcribe.TranscribeResult

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

        val imagePath = imagePath(context, diagramName)
        return TranscribeResult("![$altText]($imagePath)\n")
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

    private fun imagePath(context: ADFTranscriberContext, diagramName: String): String {
        val folder = context.suggestedImageFolder
        return "$folder/${diagramName.toSnakeCase()}"
    }
}

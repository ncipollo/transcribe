package transcribe.atlassian

import data.atlassian.adf.ExtensionNode
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
        return TranscribeResult("![$altText](images/image.png)\n")
    }

    private fun extractAltText(parameters: kotlinx.serialization.json.JsonObject?): String {
        if (parameters == null) return ""

        val macroMetadata = parameters["macroMetadata"]?.jsonObject ?: return ""
        val title = macroMetadata["title"]?.jsonPrimitive?.content ?: return ""

        return title
    }
}

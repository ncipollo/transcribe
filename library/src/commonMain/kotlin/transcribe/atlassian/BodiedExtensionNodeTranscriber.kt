package transcribe.atlassian

import data.atlassian.adf.BodiedExtensionNode
import transcribe.TranscribeResult

/**
 * Transcriber for BodiedExtensionNode that delegates to extension-type-specific transcribers
 * based on the extensionType attribute.
 */
class BodiedExtensionNodeTranscriber(
    customExtensionMapper: ExtensionMapper = ExtensionMapper(emptyMap()),
    defaultExtensionMapper: ExtensionMapper = defaultExtensionMapper(),
) : ADFTranscriber<BodiedExtensionNode> {
    private val helper = ExtensionNodeTranscriptionHelper(customExtensionMapper, defaultExtensionMapper)

    override fun transcribe(
        input: BodiedExtensionNode,
        context: ADFTranscriberContext,
    ): TranscribeResult<String> {
        return helper.transcribeExtension(input.attrs.extensionType, input.attrs.extensionKey, context) { transcriber ->
            @Suppress("UNCHECKED_CAST")
            (transcriber as? ADFTranscriber<BodiedExtensionNode>)?.transcribe(input, context)
                ?: TranscribeResult("")
        }
    }
}

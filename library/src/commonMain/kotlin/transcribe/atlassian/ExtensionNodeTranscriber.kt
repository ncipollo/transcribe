package transcribe.atlassian

import data.atlassian.adf.ExtensionNode
import transcribe.TranscribeResult

/**
 * Transcriber for ExtensionNode that delegates to extension-type-specific transcribers
 * based on the extensionType attribute.
 */
class ExtensionNodeTranscriber(
    customExtensionMapper: ExtensionMapper = ExtensionMapper(emptyMap()),
    defaultExtensionMapper: ExtensionMapper = defaultExtensionMapper(),
) : ADFTranscriber<ExtensionNode> {
    private val helper = ExtensionNodeTranscriptionHelper(customExtensionMapper, defaultExtensionMapper)

    override fun transcribe(
        input: ExtensionNode,
        context: ADFTranscriberContext,
    ): TranscribeResult<String> {
        return helper.transcribeExtension(input.attrs.extensionType, input.attrs.extensionKey, context) { transcriber ->
            @Suppress("UNCHECKED_CAST")
            (transcriber as? ADFTranscriber<ExtensionNode>)?.transcribe(input, context)
                ?: TranscribeResult("")
        }
    }
}

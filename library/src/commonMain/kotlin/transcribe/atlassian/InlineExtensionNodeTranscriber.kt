package transcribe.atlassian

import data.atlassian.adf.InlineExtensionNode
import transcribe.TranscribeResult

/**
 * Transcriber for InlineExtensionNode that delegates to extension-type-specific transcribers
 * based on the extensionType attribute.
 */
class InlineExtensionNodeTranscriber(
    customExtensionMapper: ExtensionMapper = ExtensionMapper(emptyMap()),
    defaultExtensionMapper: ExtensionMapper = defaultExtensionMapper(),
) : ADFTranscriber<InlineExtensionNode> {
    private val helper = ExtensionNodeTranscriptionHelper(customExtensionMapper, defaultExtensionMapper)

    override fun transcribe(
        input: InlineExtensionNode,
        context: ADFTranscriberContext,
    ): TranscribeResult<String> {
        return helper.transcribeExtension(input.attrs.extensionType, context) { transcriber ->
            @Suppress("UNCHECKED_CAST")
            (transcriber as? ADFTranscriber<InlineExtensionNode>)?.transcribe(input, context)
                ?: TranscribeResult("")
        }
    }
}

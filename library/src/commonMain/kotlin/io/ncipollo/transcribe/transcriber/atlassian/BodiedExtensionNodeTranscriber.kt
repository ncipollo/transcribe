package io.ncipollo.transcribe.transcriber.atlassian

import io.ncipollo.transcribe.context.ADFTranscriberContext
import io.ncipollo.transcribe.data.atlassian.adf.BodiedExtensionNode
import io.ncipollo.transcribe.transcriber.TranscribeResult

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
        return helper.transcribeExtension(input.attrs.extensionType, input.attrs.extensionKey) { transcriber ->
            @Suppress("UNCHECKED_CAST")
            (transcriber as? ADFTranscriber<BodiedExtensionNode>)?.transcribe(input, context)
                ?: TranscribeResult("")
        }
    }
}

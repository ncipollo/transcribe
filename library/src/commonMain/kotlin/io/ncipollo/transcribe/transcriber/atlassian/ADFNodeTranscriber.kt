package io.ncipollo.transcribe.transcriber.atlassian

import io.ncipollo.transcribe.context.ADFTranscriberContext
import io.ncipollo.transcribe.data.atlassian.adf.ADFNode
import io.ncipollo.transcribe.transcriber.TranscribeResult

/**
 * Dispatcher that routes ADF nodes to their appropriate transcribers.
 * This is used by block nodes to transcribe their child content.
 */
class ADFNodeTranscriber(
    private val mapper: ADFNodeMapper,
) {
    /**
     * Transcribes any ADF node to markdown string.
     */
    @Suppress("UNCHECKED_CAST")
    fun transcribe(
        node: ADFNode,
        context: ADFTranscriberContext,
    ): TranscribeResult<String> {
        val transcriber = mapper.transcriberFor(node) as? ADFTranscriber<ADFNode>
        return transcriber?.transcribe(node, context) ?: TranscribeResult("")
    }
}

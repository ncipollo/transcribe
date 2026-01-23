package io.ncipollo.transcribe.transcriber.atlassian

import io.ncipollo.transcribe.context.ADFTranscriberContext
import io.ncipollo.transcribe.data.atlassian.adf.HardBreakNode
import io.ncipollo.transcribe.transcriber.TranscribeResult

/**
 * Transcriber for HardBreakNode that converts ADF hard break to markdown newline.
 */
class HardBreakNodeTranscriber : ADFTranscriber<HardBreakNode> {
    override fun transcribe(
        input: HardBreakNode,
        context: ADFTranscriberContext,
    ): TranscribeResult<String> {
        return TranscribeResult("\n")
    }
}

package transcribe.atlassian

import data.atlassian.adf.HardBreakNode
import transcribe.TranscribeResult

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

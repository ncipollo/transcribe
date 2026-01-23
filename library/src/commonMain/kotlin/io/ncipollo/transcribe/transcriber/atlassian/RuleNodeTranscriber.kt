package io.ncipollo.transcribe.transcriber.atlassian

import io.ncipollo.transcribe.context.ADFTranscriberContext
import io.ncipollo.transcribe.data.atlassian.adf.RuleNode
import io.ncipollo.transcribe.transcriber.TranscribeResult

/**
 * Transcriber for RuleNode that converts ADF rule to markdown horizontal rule.
 * Outputs --- followed by double newline.
 */
class RuleNodeTranscriber : ADFTranscriber<RuleNode> {
    override fun transcribe(
        input: RuleNode,
        context: ADFTranscriberContext,
    ): TranscribeResult<String> {
        return TranscribeResult("---\n\n")
    }
}

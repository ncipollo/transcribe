package transcribe.atlassian

import data.atlassian.adf.RuleNode
import transcribe.TranscribeResult

/**
 * Transcriber for RuleNode that converts ADF rule to markdown horizontal rule.
 * Outputs --- followed by double newline.
 */
class RuleNodeTranscriber : ADFTranscriber<RuleNode> {
    override fun transcribe(input: RuleNode): TranscribeResult<String> {
        return TranscribeResult("---\n\n")
    }
}

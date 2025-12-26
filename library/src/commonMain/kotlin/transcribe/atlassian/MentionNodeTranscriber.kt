package transcribe.atlassian

import data.atlassian.adf.MentionNode
import transcribe.TranscribeResult

/**
 * Transcriber for MentionNode that converts ADF mention to markdown mention syntax.
 * Outputs @username format, using text if available, otherwise id.
 */
class MentionNodeTranscriber : ADFTranscriber<MentionNode> {
    override fun transcribe(
        input: MentionNode,
        context: ADFTranscriberContext,
    ): TranscribeResult<String> {
        val username = input.attrs.text ?: input.attrs.id
        return TranscribeResult("@$username")
    }
}

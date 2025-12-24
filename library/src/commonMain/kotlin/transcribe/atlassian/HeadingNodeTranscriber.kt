package transcribe.atlassian

import data.atlassian.adf.HeadingNode
import transcribe.TranscribeResult

/**
 * Transcriber for HeadingNode that converts ADF heading to markdown heading.
 * Outputs # prefix based on level (1-6), followed by content and double newline.
 */
class HeadingNodeTranscriber : ADFTranscriber<HeadingNode> {
    override fun transcribe(input: HeadingNode): TranscribeResult<String> {
        val level = input.attrs.level.coerceIn(1, 6)
        val prefix = "#".repeat(level)
        
        val content = input.content
        val markdown = if (content.isNullOrEmpty()) {
            ""
        } else {
            content.joinToString("") { node ->
                ADFNodeTranscriber.transcribeInline(node).content
            }
        }
        
        return TranscribeResult("$prefix $markdown\n\n")
    }
}


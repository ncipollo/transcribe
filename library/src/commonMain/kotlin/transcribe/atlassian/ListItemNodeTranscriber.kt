package transcribe.atlassian

import data.atlassian.adf.ADFNode
import data.atlassian.adf.ListItemNode
import transcribe.TranscribeResult

/**
 * Transcriber for ListItemNode that converts ADF list item to markdown.
 * Handles item content and nesting. The prefix (- or number) is added by parent list.
 */
class ListItemNodeTranscriber : ADFTranscriber<ListItemNode> {
    override fun transcribe(input: ListItemNode): TranscribeResult<String> {
        val content = input.content
        if (content.isEmpty()) {
            return TranscribeResult("")
        }
        
        val markdown = content.joinToString("") { node ->
            when (node) {
                is data.atlassian.adf.ADFInlineNode -> {
                    ADFNodeTranscriber.transcribeInline(node).content
                }
                is data.atlassian.adf.ADFBlockNode -> {
                    ADFNodeTranscriber.transcribeBlock(node).content
                }
            }
        }
        
        return TranscribeResult(markdown)
    }
}


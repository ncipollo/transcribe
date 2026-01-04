package transcribe.atlassian

import context.ADFTranscriberContext
import data.atlassian.adf.ListItemNode
import transcribe.TranscribeResult

/**
 * Transcriber for ListItemNode that converts ADF list item to markdown.
 * Handles item content and nesting. The prefix (- or number) is added by parent list.
 */
class ListItemNodeTranscriber(
    private val mapper: ADFNodeMapper,
) : ADFTranscriber<ListItemNode> {
    override fun transcribe(
        input: ListItemNode,
        context: ADFTranscriberContext,
    ): TranscribeResult<String> {
        val content = input.content
        if (content.isEmpty()) {
            return TranscribeResult("")
        }

        val nodeTranscriber = ADFNodeTranscriber(mapper)
        val results = content.map { node -> nodeTranscriber.transcribe(node, context) }
        val markdown = results.joinToString("") { it.content }
        val actions = results.flatMap { it.actions }
        return TranscribeResult(markdown, actions)
    }
}

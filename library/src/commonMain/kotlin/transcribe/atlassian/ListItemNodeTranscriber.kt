package transcribe.atlassian

import context.ADFTranscriberContext
import data.atlassian.adf.BulletListNode
import data.atlassian.adf.ListItemNode
import data.atlassian.adf.OrderedListNode
import data.atlassian.adf.TaskListNode
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
        val results = content.map { node ->
            // Increment level for nested lists
            val childContext = when (node) {
                is BulletListNode, is OrderedListNode, is TaskListNode -> context.copy(listLevel = context.listLevel + 1)
                else -> context
            }
            nodeTranscriber.transcribe(node, childContext)
        }
        val markdown = results.joinToString("") { it.content }
        val actions = results.flatMap { it.actions }
        return TranscribeResult(markdown, actions)
    }
}

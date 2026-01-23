package io.ncipollo.transcribe.transcriber.atlassian

import io.ncipollo.transcribe.context.ADFTranscriberContext
import io.ncipollo.transcribe.data.atlassian.adf.BlockTaskItemNode
import io.ncipollo.transcribe.data.atlassian.adf.BulletListNode
import io.ncipollo.transcribe.data.atlassian.adf.OrderedListNode
import io.ncipollo.transcribe.data.atlassian.adf.TaskListNode
import io.ncipollo.transcribe.data.atlassian.adf.TaskState
import io.ncipollo.transcribe.transcriber.TranscribeResult

/**
 * Transcriber for BlockTaskItemNode that converts ADF block task item to markdown checkbox.
 * Handles block content including nested lists. The checkbox prefix is added by parent TaskList.
 */
class BlockTaskItemNodeTranscriber(
    private val mapper: ADFNodeMapper,
) : ADFTranscriber<BlockTaskItemNode> {
    override fun transcribe(
        input: BlockTaskItemNode,
        context: ADFTranscriberContext,
    ): TranscribeResult<String> {
        val checkbox =
            if (input.attrs.state == TaskState.DONE) {
                "- [x] "
            } else {
                "- [ ] "
            }

        val content = input.content
        if (content.isEmpty()) {
            return TranscribeResult("$checkbox\n")
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
        return TranscribeResult("$checkbox$markdown", actions)
    }
}

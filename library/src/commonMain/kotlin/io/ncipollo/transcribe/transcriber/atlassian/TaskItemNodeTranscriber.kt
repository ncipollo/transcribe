package io.ncipollo.transcribe.transcriber.atlassian

import io.ncipollo.transcribe.context.ADFTranscriberContext
import io.ncipollo.transcribe.data.atlassian.adf.TaskItemNode
import io.ncipollo.transcribe.data.atlassian.adf.TaskState
import io.ncipollo.transcribe.transcriber.TranscribeResult

/**
 * Transcriber for TaskItemNode that converts ADF task item to markdown checkbox.
 * Outputs - [ ] or - [x] based on task state, followed by inline content.
 */
class TaskItemNodeTranscriber(
    private val mapper: ADFNodeMapper,
) : ADFTranscriber<TaskItemNode> {
    override fun transcribe(
        input: TaskItemNode,
        context: ADFTranscriberContext,
    ): TranscribeResult<String> {
        val checkbox =
            if (input.attrs.state == TaskState.DONE) {
                "- [x]"
            } else {
                "- [ ]"
            }

        val content = input.content
        val markdown: String
        val actions: List<io.ncipollo.transcribe.transcriber.action.TranscriberAction>
        if (content.isNullOrEmpty()) {
            markdown = ""
            actions = emptyList()
        } else {
            val nodeTranscriber = ADFNodeTranscriber(mapper)
            val results = content.map { node -> nodeTranscriber.transcribe(node, context) }
            markdown = results.joinToString("") { it.content }
            actions = results.flatMap { it.actions }
        }

        return TranscribeResult("$checkbox $markdown", actions)
    }
}

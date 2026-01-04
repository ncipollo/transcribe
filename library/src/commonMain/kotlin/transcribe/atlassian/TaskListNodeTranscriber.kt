package transcribe.atlassian

import context.ADFTranscriberContext
import data.atlassian.adf.TaskListNode
import transcribe.TranscribeResult

/**
 * Transcriber for TaskListNode that converts ADF task list to markdown task list.
 * Outputs task items, one per line.
 */
class TaskListNodeTranscriber(
    private val mapper: ADFNodeMapper,
) : ADFTranscriber<TaskListNode> {
    override fun transcribe(
        input: TaskListNode,
        context: ADFTranscriberContext,
    ): TranscribeResult<String> {
        val content = input.content
        if (content.isEmpty()) {
            return TranscribeResult("")
        }

        val nodeTranscriber = ADFNodeTranscriber(mapper)
        val results = content.map { node -> nodeTranscriber.transcribe(node, context) }
        val markdown = results.joinToString("\n") { it.content }
        val actions = results.flatMap { it.actions }
        return TranscribeResult("$markdown\n", actions)
    }
}

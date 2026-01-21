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
        val results = content.map { node ->
            // Increment level for nested task lists
            val childContext = when (node) {
                is TaskListNode -> context.copy(listLevel = context.listLevel + 1)
                else -> context
            }
            nodeTranscriber.transcribe(node, childContext)
        }
        val indent = "    ".repeat(context.listLevel)
        val markdown = results.map { result ->
            val lines = result.content.trimEnd('\n').lines()
            lines.mapIndexed { index, line ->
                if (index == 0) {
                    // First line: already has checkbox from TaskItemNodeTranscriber, add indentation
                    "$indent$line\n"
                } else {
                    // Continuation lines: already have proper indentation
                    "$line\n"
                }
            }.joinToString("")
        }.joinToString("")
        val actions = results.flatMap { it.actions }
        return TranscribeResult(markdown, actions)
    }
}

package transcribe.atlassian

import data.atlassian.adf.TaskListNode
import transcribe.TranscribeResult

/**
 * Transcriber for TaskListNode that converts ADF task list to markdown task list.
 * Outputs task items, one per line.
 */
class TaskListNodeTranscriber(
    private val mapper: ADFNodeMapper,
) : ADFTranscriber<TaskListNode> {
    override fun transcribe(input: TaskListNode): TranscribeResult<String> {
        val content = input.content
        if (content.isEmpty()) {
            return TranscribeResult("")
        }

        val nodeTranscriber = ADFNodeTranscriber(mapper)
        val markdown =
            content.joinToString("\n") { node ->
                nodeTranscriber.transcribe(node).content
            }

        return TranscribeResult("$markdown\n")
    }
}

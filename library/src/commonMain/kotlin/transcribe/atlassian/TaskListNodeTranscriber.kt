package transcribe.atlassian

import data.atlassian.adf.ADFNode
import data.atlassian.adf.TaskItemNode
import data.atlassian.adf.TaskListNode
import kotlin.reflect.KClass
import transcribe.TranscribeResult

/**
 * Transcriber for TaskListNode that converts ADF task list to markdown task list.
 * Outputs task items, one per line.
 */
class TaskListNodeTranscriber(
    private val nodeMap: Map<KClass<out ADFNode>, ADFTranscriber<*>>
) : ADFTranscriber<TaskListNode> {
    override fun transcribe(input: TaskListNode): TranscribeResult<String> {
        val content = input.content
        if (content.isEmpty()) {
            return TranscribeResult("")
        }
        
        val nodeTranscriber = ADFNodeTranscriber(nodeMap)
        val markdown = content.joinToString("\n") { node ->
            when (node) {
                is TaskItemNode -> nodeTranscriber.transcribeInline(node).content
                else -> nodeTranscriber.transcribe(node).content
            }
        }
        
        return TranscribeResult("$markdown\n\n")
    }
}


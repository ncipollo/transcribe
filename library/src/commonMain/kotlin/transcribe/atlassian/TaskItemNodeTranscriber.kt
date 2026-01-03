package transcribe.atlassian

import context.ADFTranscriberContext
import data.atlassian.adf.TaskItemNode
import data.atlassian.adf.TaskState
import transcribe.TranscribeResult

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
        val markdown =
            if (content.isNullOrEmpty()) {
                ""
            } else {
                val nodeTranscriber = ADFNodeTranscriber(mapper)
                content.joinToString("") { node ->
                    nodeTranscriber.transcribe(node, context).content
                }
            }

        return TranscribeResult("$checkbox $markdown")
    }
}

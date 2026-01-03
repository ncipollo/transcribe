package transcribe.atlassian

import context.ADFTranscriberContext
import data.atlassian.adf.TaskItemAttrs
import data.atlassian.adf.TaskItemNode
import data.atlassian.adf.TaskState
import data.atlassian.adf.TextNode
import kotlin.test.Test
import kotlin.test.assertEquals

class TaskItemNodeTranscriberTest {
    private val transcriber = TaskItemNodeTranscriber(defaultADFNodeMapper())
    private val context = ADFTranscriberContext()

    @Test
    fun transcribe_todoState() {
        val node =
            TaskItemNode(
                attrs = TaskItemAttrs(localId = "1", state = TaskState.TODO),
                content = listOf(TextNode(text = "Task text")),
            )
        val result = transcriber.transcribe(node, context)
        assertEquals("- [ ] Task text", result.content)
    }

    @Test
    fun transcribe_doneState() {
        val node =
            TaskItemNode(
                attrs = TaskItemAttrs(localId = "1", state = TaskState.DONE),
                content = listOf(TextNode(text = "Completed task")),
            )
        val result = transcriber.transcribe(node, context)
        assertEquals("- [x] Completed task", result.content)
    }

    @Test
    fun transcribe_emptyContent() {
        val node =
            TaskItemNode(
                attrs = TaskItemAttrs(localId = "1", state = TaskState.TODO),
                content = null,
            )
        val result = transcriber.transcribe(node, context)
        assertEquals("- [ ] ", result.content)
    }
}

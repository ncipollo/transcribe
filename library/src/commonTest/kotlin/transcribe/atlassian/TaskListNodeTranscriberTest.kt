package transcribe.atlassian

import data.atlassian.adf.TaskItemAttrs
import data.atlassian.adf.TaskItemNode
import data.atlassian.adf.TaskListAttrs
import data.atlassian.adf.TaskListNode
import data.atlassian.adf.TaskState
import data.atlassian.adf.TextNode
import kotlin.test.Test
import kotlin.test.assertEquals

class TaskListNodeTranscriberTest {
    private val transcriber = TaskListNodeTranscriber(defaultADFNodeMapper())

    @Test
    fun transcribe_withItems() {
        val node =
            TaskListNode(
                attrs = TaskListAttrs(localId = "1"),
                content =
                    listOf(
                        TaskItemNode(
                            attrs = TaskItemAttrs(localId = "1", state = TaskState.TODO),
                            content = listOf(TextNode(text = "Task 1")),
                        ),
                        TaskItemNode(
                            attrs = TaskItemAttrs(localId = "2", state = TaskState.DONE),
                            content = listOf(TextNode(text = "Task 2")),
                        ),
                    ),
            )
        val result = transcriber.transcribe(node)
        assertEquals("- [ ] Task 1\n- [x] Task 2\n\n", result.content)
    }

    @Test
    fun transcribe_emptyContent() {
        val node =
            TaskListNode(
                attrs = TaskListAttrs(localId = "1"),
                content = emptyList(),
            )
        val result = transcriber.transcribe(node)
        assertEquals("", result.content)
    }
}

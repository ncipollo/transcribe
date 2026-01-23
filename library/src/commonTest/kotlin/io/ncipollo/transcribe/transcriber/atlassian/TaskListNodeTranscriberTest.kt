package io.ncipollo.transcribe.transcriber.atlassian

import io.ncipollo.transcribe.context.ADFTranscriberContext
import io.ncipollo.transcribe.data.atlassian.adf.TaskItemAttrs
import io.ncipollo.transcribe.data.atlassian.adf.TaskItemNode
import io.ncipollo.transcribe.data.atlassian.adf.TaskListAttrs
import io.ncipollo.transcribe.data.atlassian.adf.TaskListNode
import io.ncipollo.transcribe.data.atlassian.adf.TaskState
import io.ncipollo.transcribe.data.atlassian.adf.TextNode
import kotlin.test.Test
import kotlin.test.assertEquals

class TaskListNodeTranscriberTest {
    private val transcriber = TaskListNodeTranscriber(defaultADFNodeMapper())
    private val context = ADFTranscriberContext()

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
        val result = transcriber.transcribe(node, context)
        assertEquals("- [ ] Task 1\n- [x] Task 2\n", result.content)
    }

    @Test
    fun transcribe_emptyContent() {
        val node =
            TaskListNode(
                attrs = TaskListAttrs(localId = "1"),
                content = emptyList(),
            )
        val result = transcriber.transcribe(node, context)
        assertEquals("", result.content)
    }
}

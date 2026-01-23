package io.ncipollo.transcribe.transcriber.markdown

import io.ncipollo.transcribe.context.MarkdownContext
import io.ncipollo.transcribe.data.atlassian.adf.TaskItemAttrs
import io.ncipollo.transcribe.data.atlassian.adf.TaskItemNode
import io.ncipollo.transcribe.data.atlassian.adf.TaskState
import io.ncipollo.transcribe.data.atlassian.adf.TextNode
import org.intellij.markdown.MarkdownElementTypes
import kotlin.test.Test
import kotlin.test.assertEquals

class CheckListItemTranscriberTest {
    private val nodeMapper = defaultMarkdownNodeMapper()
    private val transcriber = CheckListItemTranscriber(nodeMapper)

    @Test
    fun transcribe_checkListItem_withCheckboxUnchecked() {
        val markdown = "- [ ] Task item"
        val listItemNode = MarkdownTestHelper.findNestedNode(markdown, MarkdownElementTypes.LIST_ITEM)
        val context = MarkdownContext(markdownText = markdown)
        val result = transcriber.transcribe(listItemNode, context)

        val expected =
            TaskItemNode(
                attrs = TaskItemAttrs(localId = "", state = TaskState.TODO),
                content =
                listOf(
                    TextNode(text = "Task item"),
                ),
            )
        assertEquals(expected, result.content)
    }

    @Test
    fun transcribe_checkListItem_withCheckboxChecked() {
        val markdown = "- [x] Completed task"
        val listItemNode = MarkdownTestHelper.findNestedNode(markdown, MarkdownElementTypes.LIST_ITEM)
        val context = MarkdownContext(markdownText = markdown)
        val result = transcriber.transcribe(listItemNode, context)

        val expected =
            TaskItemNode(
                attrs = TaskItemAttrs(localId = "", state = TaskState.DONE),
                content =
                listOf(
                    TextNode(text = "Completed task"),
                ),
            )
        assertEquals(expected, result.content)
    }
}

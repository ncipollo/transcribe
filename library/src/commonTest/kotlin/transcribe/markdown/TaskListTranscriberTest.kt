package transcribe.markdown

import data.atlassian.adf.TaskItemAttrs
import data.atlassian.adf.TaskItemNode
import data.atlassian.adf.TaskListAttrs
import data.atlassian.adf.TaskListNode
import data.atlassian.adf.TaskState
import data.atlassian.adf.TextNode
import org.intellij.markdown.MarkdownElementTypes
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class TaskListTranscriberTest {
    private val nodeMapper = defaultMarkdownNodeMapper()
    private val transcriber = TaskListTranscriber(nodeMapper)

    @Test
    fun isTaskList_emptyList() {
        // Create a markdown that results in a list with no items
        // This is tricky - we'll use a list node that has no LIST_ITEM children
        val markdown = "- "
        val listNode = MarkdownTestHelper.findNode(markdown, MarkdownElementTypes.UNORDERED_LIST)
        val result = transcriber.isTaskList(listNode)
        assertFalse(result, "Empty list should not be a task list")
    }

    @Test
    fun isTaskList_allItemsHaveCheckboxes() {
        val markdown = "- [ ] Task 1\n- [x] Task 2\n- [ ] Task 3"
        val listNode = MarkdownTestHelper.findNode(markdown, MarkdownElementTypes.UNORDERED_LIST)
        val result = transcriber.isTaskList(listNode)
        assertTrue(result, "List with all items having checkboxes should be a task list")
    }

    @Test
    fun isTaskList_mixedItems() {
        val markdown = "- [ ] Task 1\n- Regular item\n- [x] Task 2"
        val listNode = MarkdownTestHelper.findNode(markdown, MarkdownElementTypes.UNORDERED_LIST)
        val result = transcriber.isTaskList(listNode)
        assertFalse(result, "List with mixed items (some with checkboxes, some without) should not be a task list")
    }

    @Test
    fun isTaskList_noCheckboxes() {
        val markdown = "- Item 1\n- Item 2"
        val listNode = MarkdownTestHelper.findNode(markdown, MarkdownElementTypes.UNORDERED_LIST)
        val result = transcriber.isTaskList(listNode)
        assertFalse(result, "Regular list without checkboxes should not be a task list")
    }

    @Test
    fun transcribe_bulletListWithCheckboxes() {
        val markdown = "- [ ] Unchecked task\n- [x] Checked task"
        val listNode = MarkdownTestHelper.findNode(markdown, MarkdownElementTypes.UNORDERED_LIST)
        val context = MarkdownContext(markdownText = markdown)
        val result = transcriber.transcribe(listNode, context)

        val expected =
            TaskListNode(
                attrs = TaskListAttrs(localId = ""),
                content =
                listOf(
                    TaskItemNode(
                        attrs = TaskItemAttrs(localId = "", state = TaskState.TODO),
                        content =
                        listOf(
                            TextNode(text = "Unchecked task"),
                        ),
                    ),
                    TaskItemNode(
                        attrs = TaskItemAttrs(localId = "", state = TaskState.DONE),
                        content =
                        listOf(
                            TextNode(text = "Checked task"),
                        ),
                    ),
                ),
            )
        assertEquals(expected, result.content)
    }

    @Test
    fun transcribe_orderedListWithCheckboxes() {
        val markdown = "1. [ ] First task\n2. [x] Second task"
        val listNode = MarkdownTestHelper.findNode(markdown, MarkdownElementTypes.ORDERED_LIST)
        val context = MarkdownContext(markdownText = markdown)
        val result = transcriber.transcribe(listNode, context)

        val expected =
            TaskListNode(
                attrs = TaskListAttrs(localId = ""),
                content =
                listOf(
                    TaskItemNode(
                        attrs = TaskItemAttrs(localId = "", state = TaskState.TODO),
                        content =
                        listOf(
                            TextNode(text = "First task"),
                        ),
                    ),
                    TaskItemNode(
                        attrs = TaskItemAttrs(localId = "", state = TaskState.DONE),
                        content =
                        listOf(
                            TextNode(text = "Second task"),
                        ),
                    ),
                ),
            )
        assertEquals(expected, result.content)
    }
}

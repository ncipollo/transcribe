package transcribe.markdown

import data.atlassian.adf.ListItemNode
import data.atlassian.adf.OrderedListNode
import data.atlassian.adf.ParagraphNode
import data.atlassian.adf.TextNode
import org.intellij.markdown.MarkdownElementTypes
import kotlin.test.Test
import kotlin.test.assertEquals

class OrderedListTranscriberTest {
    private val nodeMapper = defaultMarkdownNodeMapper()
    private val transcriber = OrderedListTranscriber(nodeMapper)

    @Test
    fun transcribe_orderedList() {
        val markdown = "1. Item 1\n2. Item 2"
        val listNode = MarkdownTestHelper.findNode(markdown, MarkdownElementTypes.ORDERED_LIST)
        val context = MarkdownContext(markdownText = markdown)
        val result = transcriber.transcribe(listNode, context)

        val expected =
            OrderedListNode(
                content =
                    listOf(
                        ListItemNode(
                            content =
                                listOf(
                                    ParagraphNode(
                                        content =
                                            listOf(
                                                TextNode(text = "Item 1"),
                                            ),
                                    ),
                                ),
                        ),
                        ListItemNode(
                            content =
                                listOf(
                                    ParagraphNode(
                                        content =
                                            listOf(
                                                TextNode(text = "Item 2"),
                                            ),
                                    ),
                                ),
                        ),
                    ),
            )
        assertEquals(expected, result.content)
    }
}

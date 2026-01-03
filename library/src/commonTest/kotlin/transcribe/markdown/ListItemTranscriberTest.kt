package transcribe.markdown

import context.MarkdownContext
import data.atlassian.adf.ListItemNode
import data.atlassian.adf.ParagraphNode
import data.atlassian.adf.TextNode
import org.intellij.markdown.MarkdownElementTypes
import kotlin.test.Test
import kotlin.test.assertEquals

class ListItemTranscriberTest {
    private val nodeMapper = defaultMarkdownNodeMapper()
    private val transcriber = ListItemTranscriber(nodeMapper)

    @Test
    fun transcribe_listItem_withParagraph() {
        val markdown = "- Item text"
        val listItemNode = MarkdownTestHelper.findNestedNode(markdown, MarkdownElementTypes.LIST_ITEM)
        val context = MarkdownContext(markdownText = markdown)
        val result = transcriber.transcribe(listItemNode, context)

        val expected =
            ListItemNode(
                content =
                listOf(
                    ParagraphNode(
                        content =
                        listOf(
                            TextNode(text = "Item text"),
                        ),
                    ),
                ),
            )
        assertEquals(expected, result.content)
    }
}

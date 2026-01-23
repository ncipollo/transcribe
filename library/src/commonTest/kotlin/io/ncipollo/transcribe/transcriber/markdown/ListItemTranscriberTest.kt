package io.ncipollo.transcribe.transcriber.markdown

import io.ncipollo.transcribe.context.MarkdownContext
import io.ncipollo.transcribe.data.atlassian.adf.ListItemNode
import io.ncipollo.transcribe.data.atlassian.adf.ParagraphNode
import io.ncipollo.transcribe.data.atlassian.adf.TextNode
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

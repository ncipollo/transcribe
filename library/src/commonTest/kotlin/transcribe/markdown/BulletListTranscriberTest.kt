package transcribe.markdown

import data.atlassian.adf.BulletListNode
import data.atlassian.adf.ListItemNode
import data.atlassian.adf.ParagraphNode
import data.atlassian.adf.TextNode
import org.intellij.markdown.MarkdownElementTypes
import kotlin.test.Test
import kotlin.test.assertEquals

class BulletListTranscriberTest {
    private val nodeMapper = defaultMarkdownNodeMapper()
    private val transcriber = BulletListTranscriber(nodeMapper)

    @Test
    fun transcribe_bulletList() {
        val markdown = "- Item 1\n- Item 2"
        val listNode = MarkdownTestHelper.findNode(markdown, MarkdownElementTypes.UNORDERED_LIST)
        val context = MarkdownContext(markdownText = markdown)
        val result = transcriber.transcribe(listNode, context)

        val expected =
            BulletListNode(
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

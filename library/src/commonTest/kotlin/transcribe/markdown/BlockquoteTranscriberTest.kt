package transcribe.markdown

import data.atlassian.adf.BlockquoteNode
import data.atlassian.adf.ParagraphNode
import data.atlassian.adf.TextNode
import org.intellij.markdown.MarkdownElementTypes
import kotlin.test.Test
import kotlin.test.assertEquals

class BlockquoteTranscriberTest {
    private val nodeMapper = defaultMarkdownNodeMapper()
    private val transcriber = BlockquoteTranscriber(nodeMapper)

    @Test
    fun transcribe_blockquote() {
        val markdown = "> Quote text"
        val blockquoteNode = MarkdownTestHelper.findNode(markdown, MarkdownElementTypes.BLOCK_QUOTE)
        val context = MarkdownContext(markdownText = markdown)
        val result = transcriber.transcribe(blockquoteNode, context)

        val expected =
            BlockquoteNode(
                content =
                    listOf(
                        ParagraphNode(
                            content =
                                listOf(
                                    TextNode(text = "Quote text"),
                                ),
                        ),
                    ),
            )
        assertEquals(expected, result.content)
    }
}

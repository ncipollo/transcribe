package transcribe.markdown

import context.MarkdownContext
import data.atlassian.adf.ParagraphNode
import data.atlassian.adf.TextNode
import org.intellij.markdown.MarkdownElementTypes
import kotlin.test.Test
import kotlin.test.assertEquals

class ParagraphTranscriberTest {
    private val mapper = defaultMarkdownNodeMapper()
    private val transcriber = ParagraphTranscriber(mapper)

    @Test
    fun transcribe_simpleParagraph() {
        val markdown = "Hello world"
        val paragraphNode = MarkdownTestHelper.findNode(markdown, MarkdownElementTypes.PARAGRAPH)
        val context = MarkdownContext(markdownText = markdown)
        val result = transcriber.transcribe(paragraphNode, context)

        val expected =
            ParagraphNode(
                content =
                listOf(
                    TextNode(text = "Hello world"),
                ),
            )
        assertEquals(expected, result.content)
    }
}

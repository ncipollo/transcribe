package transcribe.markdown

import data.atlassian.adf.HeadingAttrs
import data.atlassian.adf.HeadingNode
import data.atlassian.adf.TextNode
import org.intellij.markdown.MarkdownElementTypes
import kotlin.test.Test
import kotlin.test.assertEquals

class HeadingTranscriberTest {
    private val inlineTranscriber = InlineContentTranscriber()
    private val transcriber = HeadingTranscriber(inlineTranscriber)

    @Test
    fun transcribe_atx1() {
        val markdown = "# Title"
        val headingNode = MarkdownTestHelper.findNode(markdown, MarkdownElementTypes.ATX_1)
        val context = MarkdownContext(markdownText = markdown)
        val result = transcriber.transcribe(headingNode, context)

        val expected =
            HeadingNode(
                attrs = HeadingAttrs(level = 1),
                content =
                    listOf(
                        TextNode(text = "Title"),
                    ),
            )
        assertEquals(expected, result.content)
    }

    @Test
    fun transcribe_atx3() {
        val markdown = "### Subtitle"
        val headingNode = MarkdownTestHelper.findNode(markdown, MarkdownElementTypes.ATX_3)
        val context = MarkdownContext(markdownText = markdown)
        val result = transcriber.transcribe(headingNode, context)

        val expected =
            HeadingNode(
                attrs = HeadingAttrs(level = 3),
                content =
                    listOf(
                        TextNode(text = "Subtitle"),
                    ),
            )
        assertEquals(expected, result.content)
    }
}

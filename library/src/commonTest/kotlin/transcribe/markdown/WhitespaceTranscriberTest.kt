package transcribe.markdown

import data.atlassian.adf.TextNode
import org.intellij.markdown.MarkdownTokenTypes
import kotlin.test.Test
import kotlin.test.assertEquals

class WhitespaceTranscriberTest {
    private val transcriber = WhitespaceTranscriber()

    @Test
    fun transcribe_singleSpace() {
        val markdown = "# Hello world"
        val whitespaceNode = MarkdownTestHelper.findNestedNode(markdown, MarkdownTokenTypes.WHITE_SPACE)
        val context = MarkdownContext(markdownText = markdown)
        val result = transcriber.transcribe(whitespaceNode, context)

        val expected = TextNode(text = " ")
        assertEquals(expected, result.content)
    }

    @Test
    fun transcribe_multipleSpaces() {
        val markdown = "#  Hello world"
        val whitespaceNode = MarkdownTestHelper.findNestedNode(markdown, MarkdownTokenTypes.WHITE_SPACE)
        val context = MarkdownContext(markdownText = markdown)
        val result = transcriber.transcribe(whitespaceNode, context)

        val expected = TextNode(text = "  ")
        assertEquals(expected, result.content)
    }
}

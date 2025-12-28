package transcribe.markdown

import data.atlassian.adf.TextNode
import org.intellij.markdown.MarkdownTokenTypes
import kotlin.test.Test
import kotlin.test.assertEquals

class TextTranscriberTest {
    private val transcriber = TextTranscriber()

    @Test
    fun transcribe_simpleText() {
        val markdown = "Hello world"
        val textNode = MarkdownTestHelper.findNestedNode(markdown, MarkdownTokenTypes.TEXT)
        val context = MarkdownContext(markdownText = markdown)
        val result = transcriber.transcribe(textNode, context)

        val expected = TextNode(text = "Hello world")
        assertEquals(expected, result.content)
    }
}

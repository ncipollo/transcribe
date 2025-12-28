package transcribe.markdown

import org.intellij.markdown.MarkdownTokenTypes
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class TextTranscriberTest {
    private val transcriber = TextTranscriber()

    @Test
    fun transcribe_simpleText() {
        val markdown = "Hello world"
        val textNode = MarkdownTestHelper.findNestedNode(markdown, MarkdownTokenTypes.TEXT)
        val context = MarkdownContext(markdownText = markdown)
        val result = transcriber.transcribe(textNode, context)
        
        assertNotNull(result.content)
        assertEquals("Hello world", result.content.text)
    }
}


package transcribe.markdown

import data.markdown.parser.MarkdownDocument
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class TextTranscriberTest {
    private val transcriber = TextTranscriber()

    @Test
    fun transcribe_simpleText() {
        val markdown = "Hello world"
        val document = MarkdownDocument.create(markdown)
        
        // Find a TEXT node
        val textNode = document.rootNode.children
            .flatMap { it.children }
            .firstOrNull { it.type == org.intellij.markdown.MarkdownTokenTypes.TEXT }
        
        assertNotNull(textNode, "Should find text node")
        
        val context = MarkdownContext(markdownText = markdown)
        val result = transcriber.transcribe(textNode, context)
        
        assertNotNull(result.content)
        assertEquals("Hello world", result.content.text)
    }
}


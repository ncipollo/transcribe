package transcribe.markdown

import data.markdown.parser.MarkdownDocument
import org.intellij.markdown.MarkdownElementTypes
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class CodeBlockTranscriberTest {
    private val transcriber = CodeBlockTranscriber()

    @Test
    fun transcribe_codeFence() {
        val markdown = "```kotlin\nfun test() {}\n```"
        val document = MarkdownDocument.create(markdown)
        
        val codeFenceNode = document.rootNode.children
            .firstOrNull { it.type == MarkdownElementTypes.CODE_FENCE }
        
        assertNotNull(codeFenceNode, "Should find code fence node")
        
        val context = MarkdownContext(markdownText = markdown)
        val result = transcriber.transcribe(codeFenceNode, context)
        
        assertNotNull(result.content)
        assertEquals("kotlin", result.content.attrs?.language)
        assertNotNull(result.content.content)
        assertEquals(2, result.content.content!!.size) // Two lines
    }
}


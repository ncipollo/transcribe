package transcribe.markdown

import data.atlassian.adf.EmMark
import data.markdown.parser.MarkdownDocument
import org.intellij.markdown.MarkdownElementTypes
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class EmphasisTranscriberTest {
    private val inlineTranscriber = InlineContentTranscriber()
    private val transcriber = EmphasisTranscriber(inlineTranscriber)

    @Test
    fun transcribe_emphasis() {
        val markdown = "*italic* text"
        val document = MarkdownDocument.create(markdown)
        
        val emphNode = document.rootNode.children
            .flatMap { it.children }
            .firstOrNull { it.type == MarkdownElementTypes.EMPH }
        
        assertNotNull(emphNode, "Should find emphasis node")
        
        val context = MarkdownContext(markdownText = markdown)
        val result = transcriber.transcribe(emphNode, context)
        
        assertNotNull(result.content)
        assertEquals("italic", result.content.text.trim())
        assertNotNull(result.content.marks)
        assertEquals(1, result.content.marks.size)
        assertEquals(EmMark, result.content.marks[0])
    }
}


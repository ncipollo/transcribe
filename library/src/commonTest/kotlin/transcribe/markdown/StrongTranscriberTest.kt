package transcribe.markdown

import data.atlassian.adf.StrongMark
import data.markdown.parser.MarkdownDocument
import org.intellij.markdown.MarkdownElementTypes
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class StrongTranscriberTest {
    private val inlineTranscriber = InlineContentTranscriber()
    private val transcriber = StrongTranscriber(inlineTranscriber)

    @Test
    fun transcribe_strong() {
        val markdown = "**bold** text"
        val document = MarkdownDocument.create(markdown)
        
        val strongNode = document.rootNode.children
            .flatMap { it.children }
            .firstOrNull { it.type == MarkdownElementTypes.STRONG }
        
        assertNotNull(strongNode, "Should find strong node")
        
        val context = MarkdownContext(markdownText = markdown)
        val result = transcriber.transcribe(strongNode, context)
        
        assertNotNull(result.content)
        assertEquals("bold", result.content.text.trim())
        assertNotNull(result.content.marks)
        assertEquals(1, result.content.marks.size)
        assertEquals(StrongMark, result.content.marks[0])
    }
}


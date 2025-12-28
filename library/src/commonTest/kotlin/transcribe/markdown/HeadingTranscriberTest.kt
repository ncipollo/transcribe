package transcribe.markdown

import data.atlassian.adf.HeadingAttrs
import data.atlassian.adf.TextNode
import data.markdown.parser.MarkdownDocument
import org.intellij.markdown.MarkdownElementTypes
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class HeadingTranscriberTest {
    private val inlineTranscriber = InlineContentTranscriber()
    private val transcriber = HeadingTranscriber(inlineTranscriber)

    @Test
    fun transcribe_atx1() {
        val markdown = "# Title"
        val document = MarkdownDocument.create(markdown)
        
        val headingNode = document.rootNode.children
            .firstOrNull { it.type == MarkdownElementTypes.ATX_1 }
        
        assertNotNull(headingNode, "Should find heading node")
        
        val context = MarkdownContext(markdownText = markdown)
        val result = transcriber.transcribe(headingNode, context)
        
        assertNotNull(result.content)
        assertEquals(1, result.content.attrs.level)
        assertNotNull(result.content.content)
        assertEquals(1, result.content.content.size)
        assertEquals("Title", (result.content.content[0] as TextNode).text.trim())
    }

    @Test
    fun transcribe_atx3() {
        val markdown = "### Subtitle"
        val document = MarkdownDocument.create(markdown)
        
        val headingNode = document.rootNode.children
            .firstOrNull { it.type == MarkdownElementTypes.ATX_3 }
        
        assertNotNull(headingNode, "Should find heading node")
        
        val context = MarkdownContext(markdownText = markdown)
        val result = transcriber.transcribe(headingNode, context)
        
        assertNotNull(result.content)
        assertEquals(3, result.content.attrs.level)
    }
}


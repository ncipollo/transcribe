package transcribe.markdown

import data.atlassian.adf.TextNode
import org.intellij.markdown.MarkdownElementTypes
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class ParagraphTranscriberTest {
    private val inlineTranscriber = InlineContentTranscriber()
    private val transcriber = ParagraphTranscriber(inlineTranscriber)

    @Test
    fun transcribe_simpleParagraph() {
        val markdown = "Hello world"
        val paragraphNode = MarkdownTestHelper.findNode(markdown, MarkdownElementTypes.PARAGRAPH)
        val context = MarkdownContext(markdownText = markdown)
        val result = transcriber.transcribe(paragraphNode, context)
        
        assertNotNull(result.content)
        assertNotNull(result.content.content)
        assertEquals(1, result.content.content.size)
        assertEquals("Hello world", (result.content.content[0] as TextNode).text.trim())
    }
}


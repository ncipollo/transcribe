package transcribe.markdown

import data.markdown.parser.MarkdownDocument
import org.intellij.markdown.MarkdownElementTypes
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class BulletListTranscriberTest {
    private val inlineTranscriber = InlineContentTranscriber()
    private val blockTranscriber = BlockContentTranscriber(inlineTranscriber)
    private val transcriber = BulletListTranscriber(blockTranscriber)

    @Test
    fun transcribe_bulletList() {
        val markdown = "- Item 1\n- Item 2"
        val document = MarkdownDocument.create(markdown)
        
        val listNode = document.rootNode.children
            .firstOrNull { it.type == MarkdownElementTypes.UNORDERED_LIST }
        
        assertNotNull(listNode, "Should find unordered list node")
        
        val context = MarkdownContext(markdownText = markdown)
        val result = transcriber.transcribe(listNode, context)
        
        assertNotNull(result.content)
        assertEquals(2, result.content.content.size)
    }
}


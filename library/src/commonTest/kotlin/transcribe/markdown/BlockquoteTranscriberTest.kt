package transcribe.markdown

import data.markdown.parser.MarkdownDocument
import org.intellij.markdown.MarkdownElementTypes
import kotlin.test.Test
import kotlin.test.assertNotNull

class BlockquoteTranscriberTest {
    private val inlineTranscriber = InlineContentTranscriber()
    private val blockTranscriber = BlockContentTranscriber(inlineTranscriber)
    private val transcriber = BlockquoteTranscriber(blockTranscriber)

    @Test
    fun transcribe_blockquote() {
        val markdown = "> Quote text"
        val document = MarkdownDocument.create(markdown)
        
        val blockquoteNode = document.rootNode.children
            .firstOrNull { it.type == MarkdownElementTypes.BLOCK_QUOTE }
        
        assertNotNull(blockquoteNode, "Should find blockquote node")
        
        val context = MarkdownContext(markdownText = markdown)
        val result = transcriber.transcribe(blockquoteNode, context)
        
        assertNotNull(result.content)
        assertNotNull(result.content.content)
    }
}


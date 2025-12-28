package transcribe.markdown

import data.atlassian.adf.CodeMark
import data.markdown.parser.MarkdownDocument
import org.intellij.markdown.MarkdownElementTypes
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class CodeSpanTranscriberTest {
    private val transcriber = CodeSpanTranscriber()

    @Test
    fun transcribe_codeSpan() {
        val markdown = "This is `code` text"
        val document = MarkdownDocument.create(markdown)
        
        val codeSpanNode = document.rootNode.children
            .flatMap { it.children }
            .firstOrNull { it.type == MarkdownElementTypes.CODE_SPAN }
        
        assertNotNull(codeSpanNode, "Should find code span node")
        
        val context = MarkdownContext(markdownText = markdown)
        val result = transcriber.transcribe(codeSpanNode, context)
        
        assertNotNull(result.content)
        assertEquals("code", result.content.text)
        assertNotNull(result.content.marks)
        assertEquals(1, result.content.marks.size)
        assertEquals(CodeMark, result.content.marks[0])
    }
}


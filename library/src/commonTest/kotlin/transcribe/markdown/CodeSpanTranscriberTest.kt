package transcribe.markdown

import data.atlassian.adf.CodeMark
import org.intellij.markdown.MarkdownElementTypes
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class CodeSpanTranscriberTest {
    private val transcriber = CodeSpanTranscriber()

    @Test
    fun transcribe_codeSpan() {
        val markdown = "This is `code` text"
        val codeSpanNode = MarkdownTestHelper.findNestedNode(markdown, MarkdownElementTypes.CODE_SPAN)
        val context = MarkdownContext(markdownText = markdown)
        val result = transcriber.transcribe(codeSpanNode, context)
        
        assertNotNull(result.content)
        assertEquals("code", result.content.text)
        assertNotNull(result.content.marks)
        assertEquals(1, result.content.marks.size)
        assertEquals(CodeMark, result.content.marks[0])
    }
}


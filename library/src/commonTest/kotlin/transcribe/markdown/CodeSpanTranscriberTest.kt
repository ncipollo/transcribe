package transcribe.markdown

import context.MarkdownContext
import data.atlassian.adf.CodeMark
import data.atlassian.adf.TextNode
import org.intellij.markdown.MarkdownElementTypes
import kotlin.test.Test
import kotlin.test.assertEquals

class CodeSpanTranscriberTest {
    private val transcriber = CodeSpanTranscriber()

    @Test
    fun transcribe_codeSpan() {
        val markdown = "This is `code` text"
        val codeSpanNode = MarkdownTestHelper.findNestedNode(markdown, MarkdownElementTypes.CODE_SPAN)
        val context = MarkdownContext(markdownText = markdown)
        val result = transcriber.transcribe(codeSpanNode, context)

        val expected =
            TextNode(
                text = "code",
                marks = listOf(CodeMark),
            )
        assertEquals(expected, result.content)
    }
}

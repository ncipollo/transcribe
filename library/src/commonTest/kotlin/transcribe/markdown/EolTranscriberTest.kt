package transcribe.markdown

import context.MarkdownContext
import data.atlassian.adf.HardBreakNode
import org.intellij.markdown.MarkdownTokenTypes
import kotlin.test.Test
import kotlin.test.assertEquals

class EolTranscriberTest {
    private val transcriber = EolTranscriber()

    @Test
    fun transcribe_eol() {
        val markdown = "Line 1\nLine 2"
        val eolNode = MarkdownTestHelper.findNestedNode(markdown, MarkdownTokenTypes.EOL)
        val context = MarkdownContext(markdownText = markdown)
        val result = transcriber.transcribe(eolNode, context)

        val expected = HardBreakNode()
        assertEquals(expected, result.content)
    }
}

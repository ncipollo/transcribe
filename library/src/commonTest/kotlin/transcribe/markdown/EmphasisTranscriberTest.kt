package transcribe.markdown

import data.atlassian.adf.EmMark
import data.atlassian.adf.TextNode
import org.intellij.markdown.MarkdownElementTypes
import kotlin.test.Test
import kotlin.test.assertEquals

class EmphasisTranscriberTest {
    private val transcriber = EmphasisTranscriber()

    @Test
    fun transcribe_italicText() {
        val markdown = "*italic*"
        val emphNode = MarkdownTestHelper.findNestedNode(markdown, MarkdownElementTypes.EMPH)
        val context = MarkdownContext(markdownText = markdown)
        val result = transcriber.transcribe(emphNode, context)

        val expected = TextNode(text = "italic", marks = listOf(EmMark))
        assertEquals(expected, result.content)
    }
}


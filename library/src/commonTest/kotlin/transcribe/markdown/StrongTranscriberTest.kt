package transcribe.markdown

import data.atlassian.adf.StrongMark
import data.atlassian.adf.TextNode
import org.intellij.markdown.MarkdownElementTypes
import kotlin.test.Test
import kotlin.test.assertEquals

class StrongTranscriberTest {
    private val transcriber = StrongTranscriber()

    @Test
    fun transcribe_boldText() {
        val markdown = "**bold**"
        val strongNode = MarkdownTestHelper.findNestedNode(markdown, MarkdownElementTypes.STRONG)
        val context = MarkdownContext(markdownText = markdown)
        val result = transcriber.transcribe(strongNode, context)

        val expected = TextNode(text = "bold", marks = listOf(StrongMark))
        assertEquals(expected, result.content)
    }
}

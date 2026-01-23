package io.ncipollo.transcribe.transcriber.markdown

import io.ncipollo.transcribe.context.MarkdownContext
import io.ncipollo.transcribe.data.atlassian.adf.StrongMark
import io.ncipollo.transcribe.data.atlassian.adf.TextNode
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

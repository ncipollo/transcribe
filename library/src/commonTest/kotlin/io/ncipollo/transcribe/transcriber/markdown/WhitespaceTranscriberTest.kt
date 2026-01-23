package io.ncipollo.transcribe.transcriber.markdown

import io.ncipollo.transcribe.context.MarkdownContext
import io.ncipollo.transcribe.data.atlassian.adf.TextNode
import org.intellij.markdown.MarkdownTokenTypes
import kotlin.test.Test
import kotlin.test.assertEquals

class WhitespaceTranscriberTest {
    private val transcriber = WhitespaceTranscriber()

    @Test
    fun transcribe_singleSpace() {
        val markdown = "# Hello world"
        val whitespaceNode = MarkdownTestHelper.findNestedNode(markdown, MarkdownTokenTypes.WHITE_SPACE)
        val context = MarkdownContext(markdownText = markdown)
        val result = transcriber.transcribe(whitespaceNode, context)

        val expected = TextNode(text = " ")
        assertEquals(expected, result.content)
    }

    @Test
    fun transcribe_multipleSpaces() {
        val markdown = "#  Hello world"
        val whitespaceNode = MarkdownTestHelper.findNestedNode(markdown, MarkdownTokenTypes.WHITE_SPACE)
        val context = MarkdownContext(markdownText = markdown)
        val result = transcriber.transcribe(whitespaceNode, context)

        val expected = TextNode(text = "  ")
        assertEquals(expected, result.content)
    }
}

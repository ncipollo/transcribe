package io.ncipollo.transcribe.transcriber.markdown

import io.ncipollo.transcribe.context.MarkdownContext
import io.ncipollo.transcribe.data.atlassian.adf.EmMark
import io.ncipollo.transcribe.data.atlassian.adf.ParagraphNode
import io.ncipollo.transcribe.data.atlassian.adf.StrongMark
import io.ncipollo.transcribe.data.atlassian.adf.TextNode
import org.intellij.markdown.MarkdownElementTypes
import kotlin.test.Test
import kotlin.test.assertEquals

class ParagraphTranscriberTest {
    private val mapper = defaultMarkdownNodeMapper()
    private val transcriber = ParagraphTranscriber(mapper)

    @Test
    fun transcribe_simpleParagraph() {
        val markdown = "Hello world"
        val paragraphNode = MarkdownTestHelper.findNode(markdown, MarkdownElementTypes.PARAGRAPH)
        val context = MarkdownContext(markdownText = markdown)
        val result = transcriber.transcribe(paragraphNode, context)

        val expected =
            ParagraphNode(
                content =
                listOf(
                    TextNode(text = "Hello world"),
                ),
            )
        assertEquals(expected, result.content)
    }

    @Test
    fun transcribe_nestedEmphasisAndStrong() {
        val markdown = "_An emphasized sentence with **bold** words._"
        val paragraphNode = MarkdownTestHelper.findNode(markdown, MarkdownElementTypes.PARAGRAPH)
        val context = MarkdownContext(markdownText = markdown)

        val result = transcriber.transcribe(paragraphNode, context)

        val expected = ParagraphNode(
            content = listOf(
                TextNode(text = "An emphasized sentence with", marks = listOf(EmMark)),
                TextNode(text = " ", marks = listOf(EmMark)),
                TextNode(text = "bold", marks = listOf(EmMark, StrongMark)),
                TextNode(text = " ", marks = listOf(EmMark)),
                TextNode(text = "words.", marks = listOf(EmMark))
            )
        )
        assertEquals(expected, result.content)
    }
}

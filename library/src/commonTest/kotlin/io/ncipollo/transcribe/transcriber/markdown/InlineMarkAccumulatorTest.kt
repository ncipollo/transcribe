package io.ncipollo.transcribe.transcriber.markdown

import io.ncipollo.transcribe.context.MarkdownContext
import io.ncipollo.transcribe.data.atlassian.adf.CodeMark
import io.ncipollo.transcribe.data.atlassian.adf.EmMark
import io.ncipollo.transcribe.data.atlassian.adf.StrikeMark
import io.ncipollo.transcribe.data.atlassian.adf.StrongMark
import io.ncipollo.transcribe.data.atlassian.adf.TextNode
import org.intellij.markdown.MarkdownElementTypes
import kotlin.test.Test
import kotlin.test.assertEquals

class InlineMarkAccumulatorTest {
    private val mapper = defaultMarkdownNodeMapper()
    private val accumulator = InlineMarkAccumulator(mapper)

    @Test
    fun transcribeWithMarks_nestedEmphasisAndStrong() {
        val markdown = "_An emphasized sentence with **bold** words._"
        val paragraphNode = MarkdownTestHelper.findNode(markdown, MarkdownElementTypes.PARAGRAPH)
        val context = MarkdownContext(markdownText = markdown)

        val result = accumulator.transcribeWithMarks(paragraphNode, context)

        val expected = listOf(
            TextNode(text = "An emphasized sentence with", marks = listOf(EmMark)),
            TextNode(text = " ", marks = listOf(EmMark)),
            TextNode(text = "bold", marks = listOf(EmMark, StrongMark)),
            TextNode(text = " ", marks = listOf(EmMark)),
            TextNode(text = "words.", marks = listOf(EmMark))
        )
        assertEquals(expected, result.content)
    }

    @Test
    fun transcribeWithMarks_tripleNesting() {
        val markdown = "_italic **bold ~~strike~~**_"
        val paragraphNode = MarkdownTestHelper.findNode(markdown, MarkdownElementTypes.PARAGRAPH)
        val context = MarkdownContext(markdownText = markdown)

        val result = accumulator.transcribeWithMarks(paragraphNode, context)

        val expected = listOf(
            TextNode(text = "italic", marks = listOf(EmMark)),
            TextNode(text = " ", marks = listOf(EmMark)),
            TextNode(text = "bold", marks = listOf(EmMark, StrongMark)),
            TextNode(text = " ", marks = listOf(EmMark, StrongMark)),
            TextNode(text = "strike", marks = listOf(EmMark, StrongMark, StrikeMark))
        )
        assertEquals(expected, result.content)
    }

    @Test
    fun transcribeWithMarks_adjacentFormatting() {
        val markdown = "**bold** and _italic_"
        val paragraphNode = MarkdownTestHelper.findNode(markdown, MarkdownElementTypes.PARAGRAPH)
        val context = MarkdownContext(markdownText = markdown)

        val result = accumulator.transcribeWithMarks(paragraphNode, context)

        val expected = listOf(
            TextNode(text = "bold", marks = listOf(StrongMark)),
            TextNode(text = " "),
            TextNode(text = "and"),
            TextNode(text = " "),
            TextNode(text = "italic", marks = listOf(EmMark))
        )
        assertEquals(expected, result.content)
    }

    @Test
    fun transcribeWithMarks_plainText() {
        val markdown = "Just plain text"
        val paragraphNode = MarkdownTestHelper.findNode(markdown, MarkdownElementTypes.PARAGRAPH)
        val context = MarkdownContext(markdownText = markdown)

        val result = accumulator.transcribeWithMarks(paragraphNode, context)

        val expected = listOf(
            TextNode(text = "Just plain text")
        )
        assertEquals(expected, result.content)
    }

    @Test
    fun transcribeWithMarks_strongOnly() {
        val markdown = "**bold text**"
        val paragraphNode = MarkdownTestHelper.findNode(markdown, MarkdownElementTypes.PARAGRAPH)
        val context = MarkdownContext(markdownText = markdown)

        val result = accumulator.transcribeWithMarks(paragraphNode, context)

        val expected = listOf(
            TextNode(text = "bold text", marks = listOf(StrongMark))
        )
        assertEquals(expected, result.content)
    }

    @Test
    fun transcribeWithMarks_emphasisOnly() {
        val markdown = "_italic text_"
        val paragraphNode = MarkdownTestHelper.findNode(markdown, MarkdownElementTypes.PARAGRAPH)
        val context = MarkdownContext(markdownText = markdown)

        val result = accumulator.transcribeWithMarks(paragraphNode, context)

        val expected = listOf(
            TextNode(text = "italic text", marks = listOf(EmMark))
        )
        assertEquals(expected, result.content)
    }

    @Test
    fun transcribeWithMarks_strikethroughOnly() {
        val markdown = "~~strikethrough text~~"
        val paragraphNode = MarkdownTestHelper.findNode(markdown, MarkdownElementTypes.PARAGRAPH)
        val context = MarkdownContext(markdownText = markdown)

        val result = accumulator.transcribeWithMarks(paragraphNode, context)

        val expected = listOf(
            TextNode(text = "strikethrough text", marks = listOf(StrikeMark))
        )
        assertEquals(expected, result.content)
    }

    @Test
    fun transcribeWithMarks_strongWithPunctuation() {
        val markdown = "**bold, text**"
        val paragraphNode = MarkdownTestHelper.findNode(markdown, MarkdownElementTypes.PARAGRAPH)
        val context = MarkdownContext(markdownText = markdown)

        val result = accumulator.transcribeWithMarks(paragraphNode, context)

        val expected = listOf(
            TextNode(text = "bold,", marks = listOf(StrongMark)),
            TextNode(text = " ", marks = listOf(StrongMark)),
            TextNode(text = "text", marks = listOf(StrongMark))
        )
        assertEquals(expected, result.content)
    }

    @Test
    fun transcribeWithMarks_codeSpanNestedInStrong() {
        val markdown = "**bold with `code` inside**"
        val paragraphNode = MarkdownTestHelper.findNode(markdown, MarkdownElementTypes.PARAGRAPH)
        val context = MarkdownContext(markdownText = markdown)

        val result = accumulator.transcribeWithMarks(paragraphNode, context)

        // Code spans should NOT inherit marks from parent formatting (StrongMark)
        val expected = listOf(
            TextNode(text = "bold with", marks = listOf(StrongMark)),
            TextNode(text = " ", marks = listOf(StrongMark)),
            TextNode(text = "code", marks = listOf(CodeMark)),
            TextNode(text = " ", marks = listOf(StrongMark)),
            TextNode(text = "inside", marks = listOf(StrongMark))
        )
        assertEquals(expected, result.content)
    }
}

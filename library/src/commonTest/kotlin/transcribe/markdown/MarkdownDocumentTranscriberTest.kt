package transcribe.markdown

import data.atlassian.adf.DocNode
import data.atlassian.adf.HeadingAttrs
import data.atlassian.adf.HeadingNode
import data.atlassian.adf.ParagraphNode
import data.atlassian.adf.TextNode
import data.markdown.parser.MarkdownDocument
import kotlin.test.Test
import kotlin.test.assertEquals

class MarkdownDocumentTranscriberTest {
    private val mapper = defaultMarkdownNodeMapper()
    private val transcriber = MarkdownDocumentTranscriber(mapper)

    @Test
    fun transcribe_simpleParagraph() {
        val markdown = "Hello world"
        val document = MarkdownDocument.create(markdown)
        val context = MarkdownContext(markdownText = markdown)
        val result = transcriber.transcribe(document.rootNode, context)

        val expected =
            DocNode(
                version = 1,
                content =
                listOf(
                    ParagraphNode(
                        content =
                        listOf(
                            TextNode(text = "Hello world"),
                        ),
                    ),
                ),
            )
        assertEquals(expected, result.content)
    }

    @Test
    fun transcribe_multipleParagraphs() {
        val markdown =
            """
            |First paragraph
            |
            |Second paragraph
            """.trimMargin()
        val document = MarkdownDocument.create(markdown)
        val context = MarkdownContext(markdownText = markdown)
        val result = transcriber.transcribe(document.rootNode, context)

        val expected =
            DocNode(
                version = 1,
                content =
                listOf(
                    ParagraphNode(
                        content =
                        listOf(
                            TextNode(text = "First paragraph"),
                        ),
                    ),
                    ParagraphNode(
                        content =
                        listOf(
                            TextNode(text = "Second paragraph"),
                        ),
                    ),
                ),
            )
        assertEquals(expected, result.content)
    }

    @Test
    fun transcribe_headingAndParagraph() {
        val markdown =
            """
            |# Title
            |
            |Some content
            """.trimMargin()
        val document = MarkdownDocument.create(markdown)
        val context = MarkdownContext(markdownText = markdown)
        val result = transcriber.transcribe(document.rootNode, context)

        val expected =
            DocNode(
                version = 1,
                content =
                listOf(
                    HeadingNode(
                        attrs = HeadingAttrs(level = 1),
                        content =
                        listOf(
                            TextNode(text = "Title"),
                        ),
                    ),
                    ParagraphNode(
                        content =
                        listOf(
                            TextNode(text = "Some content"),
                        ),
                    ),
                ),
            )
        assertEquals(expected, result.content)
    }

    @Test
    fun transcribe_emptyDocument() {
        val markdown = ""
        val document = MarkdownDocument.create(markdown)
        val context = MarkdownContext(markdownText = markdown)
        val result = transcriber.transcribe(document.rootNode, context)

        val expected =
            DocNode(
                version = 1,
                content = emptyList(),
            )
        assertEquals(expected, result.content)
    }
}

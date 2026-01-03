package transcribe.markdown

import data.atlassian.adf.DocNode
import data.atlassian.adf.ParagraphNode
import data.atlassian.adf.TextNode
import data.markdown.parser.getTextContent
import fixtures.markdown.ComplexMarkdownFixture
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class MarkdownToConfluenceTranscriberTest {
    private val context = MarkdownContext()

    @Test
    fun transcribe_withEmptyBuilder() {
        val transcriber = MarkdownToConfluenceTranscriber(EmptyMarkdownTranscriberMapBuilder())
        val markdown = "Hello\n\nWorld"
        val result = transcriber.transcribe(markdown, context.copy(markdownText = markdown))

        val expected =
            DocNode(
                version = 1,
                content =
                listOf(
                    ParagraphNode(content = listOf(TextNode(text = "Hello"))),
                    ParagraphNode(content = listOf(TextNode(text = "World"))),
                ),
            )
        assertEquals(expected, result.content)
    }

    @Test
    fun transcribe_withCustomTranscriberOverride() {
        // Create a custom Text transcriber that adds a prefix
        val customTextTranscriber =
            object : MarkdownTranscriber<TextNode> {
                override fun transcribe(
                    input: org.intellij.markdown.ast.ASTNode,
                    context: MarkdownContext,
                ): transcribe.TranscribeResult<TextNode> {
                    val text = input.getTextContent(context.markdownText).toString()
                    return transcribe.TranscribeResult(TextNode(text = "[CUSTOM]$text"))
                }
            }

        val customBuilder =
            MarkdownNodeMapperBuilder()
                .add(org.intellij.markdown.MarkdownTokenTypes.TEXT) { customTextTranscriber }

        val transcriber = MarkdownToConfluenceTranscriber(customBuilder)
        val markdown = "Hello"
        val result = transcriber.transcribe(markdown, context.copy(markdownText = markdown))

        val expected =
            DocNode(
                version = 1,
                content =
                listOf(
                    ParagraphNode(content = listOf(TextNode(text = "[CUSTOM]Hello"))),
                ),
            )
        // Should use custom transcriber instead of default
        assertEquals(expected, result.content)
    }

    @Test
    fun transcribe_emptyContent() {
        val transcriber = MarkdownToConfluenceTranscriber(EmptyMarkdownTranscriberMapBuilder())
        val markdown = ""
        val result = transcriber.transcribe(markdown, context.copy(markdownText = markdown))

        val expected =
            DocNode(
                version = 1,
                content = emptyList(),
            )
        assertEquals(expected, result.content)
    }

    @Test
    fun transcribe_complexFixtureDocument() {
        val transcriber = MarkdownToConfluenceTranscriber(EmptyMarkdownTranscriberMapBuilder())
        val markdown = ComplexMarkdownFixture.COMPLEX_MARKDOWN
        val result = transcriber.transcribe(markdown, context.copy(markdownText = markdown))

        // Verify that the result is a valid DocNode with content
        assertEquals(1, result.content.version)
        // The complex fixture should produce multiple content nodes
        // We don't assert exact structure here as it's complex, just verify it's not empty
        assertTrue(result.content.content.isNotEmpty())
    }
}

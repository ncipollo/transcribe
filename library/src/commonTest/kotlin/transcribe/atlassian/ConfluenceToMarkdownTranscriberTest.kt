package transcribe.atlassian

import data.atlassian.adf.DocNode
import data.atlassian.adf.ParagraphNode
import data.atlassian.adf.TextNode
import kotlin.test.Test
import kotlin.test.assertEquals
import transcribe.EmptyTranscriberMapBuilder
import transcribe.TranscriberMapBuilder

class ConfluenceToMarkdownTranscriberTest {

    @Test
    fun transcribe_withEmptyBuilder() {
        val transcriber = ConfluenceToMarkdownTranscriber(EmptyTranscriberMapBuilder())
        val node = DocNode(
            content = listOf(
                ParagraphNode(content = listOf(TextNode(text = "Hello"))),
                ParagraphNode(content = listOf(TextNode(text = "World")))
            )
        )
        val result = transcriber.transcribe(node)
        assertEquals("Hello\nWorld\n", result.content)
    }

    @Test
    fun transcribe_withCustomTranscriberOverride() {
        // Create a custom TextNode transcriber that adds a prefix
        val customTextTranscriber = object : ADFTranscriber<TextNode> {
            override fun transcribe(input: TextNode): transcribe.TranscribeResult<String> {
                return transcribe.TranscribeResult("[CUSTOM]${input.text}")
            }
        }

        val customBuilder = TranscriberMapBuilder<data.atlassian.adf.ADFNode, ADFTranscriber<*>>()
            .add<TextNode>(customTextTranscriber)

        val transcriber = ConfluenceToMarkdownTranscriber(customBuilder)
        val node = DocNode(
            content = listOf(
                ParagraphNode(content = listOf(TextNode(text = "Hello")))
            )
        )
        val result = transcriber.transcribe(node)
        // Should use custom transcriber instead of default
        assertEquals("[CUSTOM]Hello\n", result.content)
    }

    @Test
    fun transcribe_emptyContent() {
        val transcriber = ConfluenceToMarkdownTranscriber(EmptyTranscriberMapBuilder())
        val node = DocNode(content = emptyList())
        val result = transcriber.transcribe(node)
        assertEquals("", result.content)
    }
}


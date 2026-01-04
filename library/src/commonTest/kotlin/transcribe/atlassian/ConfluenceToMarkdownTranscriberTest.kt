package transcribe.atlassian

import api.atlassian.Attachment
import context.ADFTranscriberContext
import context.AttachmentContext
import data.atlassian.adf.ADFSerializer
import data.atlassian.adf.DocNode
import data.atlassian.adf.ParagraphNode
import data.atlassian.adf.TextNode
import fixtures.adf.ComplexADFDocumentFixture
import fixtures.markdown.ComplexMarkdownFixture
import transcribe.TranscribeResult
import transcribe.action.AttachmentDownload
import kotlin.test.Test
import kotlin.test.assertEquals

class ConfluenceToMarkdownTranscriberTest {
    private val testAttachment = Attachment(
        id = "att1",
        status = "current",
        title = "Test Image.PNG",
        createdAt = "2024-01-01T00:00:00Z",
        mediaType = "image/png",
        fileSize = 1024L,
        fileId = "8dfdd993-f45f-48ea-bde6-ac89319cbc37",
        downloadLink = "some/path/to/image.png",
    )

    private val context = ADFTranscriberContext(
        attachmentContext = AttachmentContext.from(listOf(testAttachment)),
    )

    @Test
    fun transcribe_withEmptyBuilder() {
        val transcriber = ConfluenceToMarkdownTranscriber(EmptyADFTranscriberMapBuilder())
        val node =
            DocNode(
                content =
                listOf(
                    ParagraphNode(content = listOf(TextNode(text = "Hello"))),
                    ParagraphNode(content = listOf(TextNode(text = "World"))),
                ),
            )
        val result = transcriber.transcribe(node, context)
        assertEquals("Hello\nWorld\n", result.content)
    }

    @Test
    fun transcribe_withCustomTranscriberOverride() {
        // Create a custom TextNode transcriber that adds a prefix
        val customTextTranscriber =
            object : ADFTranscriber<TextNode> {
                override fun transcribe(
                    input: TextNode,
                    context: ADFTranscriberContext,
                ): transcribe.TranscribeResult<String> {
                    return transcribe.TranscribeResult("[CUSTOM]${input.text}")
                }
            }

        val customBuilder =
            ADFNodeMapperBuilder()
                .add<TextNode> { customTextTranscriber }

        val transcriber = ConfluenceToMarkdownTranscriber(customBuilder)
        val node =
            DocNode(
                content =
                listOf(
                    ParagraphNode(content = listOf(TextNode(text = "Hello"))),
                ),
            )
        val result = transcriber.transcribe(node, context)
        // Should use custom transcriber instead of default
        assertEquals("[CUSTOM]Hello\n", result.content)
    }

    @Test
    fun transcribe_emptyContent() {
        val transcriber = ConfluenceToMarkdownTranscriber(EmptyADFTranscriberMapBuilder())
        val node = DocNode(content = emptyList())
        val result = transcriber.transcribe(node, context)
        assertEquals("", result.content)
    }

    @Test
    fun transcribe_complexFixtureDocument() {
        val transcriber = ConfluenceToMarkdownTranscriber(EmptyADFTranscriberMapBuilder())
        val node = ADFSerializer.fromJson(ComplexADFDocumentFixture.COMPLEX_DOCUMENT)
        val expected = TranscribeResult(
            content = ComplexMarkdownFixture.COMPLEX_MARKDOWN,
            actions = listOf(
                AttachmentDownload(
                    downloadPath = "some/path/to/image.png",
                    localRelativePath = "images/att1_test_image.png",
                ),
            ),
        )
        val result = transcriber.transcribe(node, context)
        assertEquals(expected, result)
    }
}

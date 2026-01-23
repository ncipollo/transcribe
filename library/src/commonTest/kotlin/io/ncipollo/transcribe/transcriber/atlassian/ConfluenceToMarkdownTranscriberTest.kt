package io.ncipollo.transcribe.transcriber.atlassian

import io.ncipollo.transcribe.api.atlassian.Attachment
import io.ncipollo.transcribe.context.ADFTranscriberContext
import io.ncipollo.transcribe.context.AttachmentContext
import io.ncipollo.transcribe.data.atlassian.adf.ADFSerializer
import io.ncipollo.transcribe.data.atlassian.adf.DocNode
import io.ncipollo.transcribe.data.atlassian.adf.ParagraphNode
import io.ncipollo.transcribe.data.atlassian.adf.TextNode
import io.ncipollo.transcribe.fixtures.adf.ComplexADFDocumentFixture
import io.ncipollo.transcribe.fixtures.markdown.ComplexMarkdownFixture
import io.ncipollo.transcribe.transcriber.TranscribeResult
import io.ncipollo.transcribe.transcriber.action.AttachmentDownload
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
                ): io.ncipollo.transcribe.transcriber.TranscribeResult<String> {
                    return io.ncipollo.transcribe.transcriber.TranscribeResult("[CUSTOM]${input.text}")
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
                AttachmentDownload(
                    downloadPath = "https://ac.draw.io/connectImage?mVer=2&zoom=1&simple=0&inComment=0&custContentId=5384339671&pageId=5385126043&lbox=1&diagramDisplayName=Untitled+Diagram-1766526257160.drawio&contentVer=1&revision=1&baseUrl=https%3A%2F%transcribe.atlassian.net%2Fwiki&diagramName=Untitled+Diagram-1766526257160.drawio&pCenter=0&width=281&links=&tbstyle=&height=391",
                    localRelativePath = "images/untitled_diagram-1766526257160.drawio",
                ),
            ),
        )
        val result = transcriber.transcribe(node, context)
        assertEquals(expected, result)
    }
}

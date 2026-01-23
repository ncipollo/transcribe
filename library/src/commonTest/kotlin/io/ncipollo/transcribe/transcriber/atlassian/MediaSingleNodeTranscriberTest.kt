package io.ncipollo.transcribe.transcriber.atlassian

import io.ncipollo.transcribe.api.atlassian.Attachment
import io.ncipollo.transcribe.context.ADFTranscriberContext
import io.ncipollo.transcribe.context.AttachmentContext
import io.ncipollo.transcribe.context.PageContext
import io.ncipollo.transcribe.data.atlassian.adf.MediaAttrs
import io.ncipollo.transcribe.data.atlassian.adf.MediaNode
import io.ncipollo.transcribe.data.atlassian.adf.MediaSingleNode
import io.ncipollo.transcribe.data.atlassian.adf.MediaType
import io.ncipollo.transcribe.transcriber.TranscribeResult
import io.ncipollo.transcribe.transcriber.action.AttachmentDownload
import kotlin.test.Test
import kotlin.test.assertEquals

class MediaSingleNodeTranscriberTest {
    private val transcriber = MediaSingleNodeTranscriber()

    private val pageContext = PageContext(title = "Test Page")
    private val testAttachment = Attachment(
        id = "att1",
        status = "current",
        title = "Test Image.PNG",
        createdAt = "2024-01-01T00:00:00Z",
        mediaType = "image/png",
        fileSize = 1024L,
        fileId = "8dfdd993-f45f-48ea-bde6-ac89319cbc37",
        downloadLink = "https://example.com/download/att1",
    )

    private val context = ADFTranscriberContext(
        attachmentContext = AttachmentContext.from(listOf(testAttachment)),
        pageContext = pageContext,
    )

    @Test
    fun transcribe_withAltText() {
        val node =
            MediaSingleNode(
                content =
                listOf(
                    MediaNode(
                        attrs =
                        MediaAttrs(
                            type = MediaType.FILE,
                            alt = "Test",
                            id = "8dfdd993-f45f-48ea-bde6-ac89319cbc37",
                            collection = "contentId-5385126043",
                        ),
                    ),
                ),
            )
        val expected = TranscribeResult(
            content = "![Test](test_page/att1_test_image.png)\n",
            actions = listOf(
                AttachmentDownload(
                    downloadPath = "https://example.com/download/att1",
                    localRelativePath = "test_page/att1_test_image.png",
                ),
            ),
        )
        val result = transcriber.transcribe(node, context)
        assertEquals(expected, result)
    }

    @Test
    fun transcribe_withoutAltText() {
        val node =
            MediaSingleNode(
                content =
                listOf(
                    MediaNode(
                        attrs =
                        MediaAttrs(
                            type = MediaType.FILE,
                            id = "8dfdd993-f45f-48ea-bde6-ac89319cbc37",
                            collection = "contentId-5385126043",
                        ),
                    ),
                ),
            )
        val expected = TranscribeResult(
            content = "![](test_page/att1_test_image.png)\n",
            actions = listOf(
                AttachmentDownload(
                    downloadPath = "https://example.com/download/att1",
                    localRelativePath = "test_page/att1_test_image.png",
                ),
            ),
        )
        val result = transcriber.transcribe(node, context)
        assertEquals(expected, result)
    }

    @Test
    fun transcribe_emptyContent() {
        val node = MediaSingleNode(content = emptyList())
        val expected = TranscribeResult("")
        val result = transcriber.transcribe(node, context)
        assertEquals(expected, result)
    }

    @Test
    fun transcribe_missingAttachment() {
        val emptyContext = ADFTranscriberContext(
            attachmentContext = AttachmentContext.from(emptyList()),
        )
        val node =
            MediaSingleNode(
                content =
                listOf(
                    MediaNode(
                        attrs =
                        MediaAttrs(
                            type = MediaType.FILE,
                            alt = "Test",
                            id = "8dfdd993-f45f-48ea-bde6-ac89319cbc37",
                            collection = "contentId-5385126043",
                        ),
                    ),
                ),
            )
        val expected = TranscribeResult("")
        val result = transcriber.transcribe(node, emptyContext)
        assertEquals(expected, result)
    }

    @Test
    fun transcribe_missingDownloadLink() {
        val attachmentWithoutDownloadLink = Attachment(
            id = "att1",
            status = "current",
            title = "Test Image.PNG",
            createdAt = "2024-01-01T00:00:00Z",
            mediaType = "image/png",
            fileSize = 1024L,
            fileId = "8dfdd993-f45f-48ea-bde6-ac89319cbc37",
            downloadLink = null,
        )
        val contextWithoutDownloadLink = ADFTranscriberContext(
            attachmentContext = AttachmentContext.from(listOf(attachmentWithoutDownloadLink)),
            pageContext = pageContext,
        )
        val node =
            MediaSingleNode(
                content =
                listOf(
                    MediaNode(
                        attrs =
                        MediaAttrs(
                            type = MediaType.FILE,
                            alt = "Test",
                            id = "8dfdd993-f45f-48ea-bde6-ac89319cbc37",
                            collection = "contentId-5385126043",
                        ),
                    ),
                ),
            )
        val expected = TranscribeResult("")
        val result = transcriber.transcribe(node, contextWithoutDownloadLink)
        assertEquals(expected, result)
    }
}

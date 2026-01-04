package transcribe.atlassian

import api.atlassian.Attachment
import context.ADFTranscriberContext
import context.AttachmentContext
import context.PageContext
import data.atlassian.adf.MediaAttrs
import data.atlassian.adf.MediaNode
import data.atlassian.adf.MediaSingleNode
import data.atlassian.adf.MediaType
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
        val result = transcriber.transcribe(node, context)
        assertEquals("![Test](test_page/att1_test_image.png)\n", result.content)
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
        val result = transcriber.transcribe(node, context)
        assertEquals("![](test_page/att1_test_image.png)\n", result.content)
    }

    @Test
    fun transcribe_emptyContent() {
        val node = MediaSingleNode(content = emptyList())
        val result = transcriber.transcribe(node, context)
        assertEquals("", result.content)
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
        val result = transcriber.transcribe(node, emptyContext)
        assertEquals("", result.content)
    }
}

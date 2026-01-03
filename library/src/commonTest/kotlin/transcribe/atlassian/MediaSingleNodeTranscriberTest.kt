package transcribe.atlassian

import context.ADFTranscriberContext
import data.atlassian.adf.MediaAttrs
import data.atlassian.adf.MediaNode
import data.atlassian.adf.MediaSingleNode
import data.atlassian.adf.MediaType
import kotlin.test.Test
import kotlin.test.assertEquals

class MediaSingleNodeTranscriberTest {
    private val transcriber = MediaSingleNodeTranscriber()
    private val context = ADFTranscriberContext()

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
                            alt = "Tony",
                            id = "8dfdd993-f45f-48ea-bde6-ac89319cbc37",
                            collection = "contentId-5385126043",
                        ),
                    ),
                ),
            )
        val result = transcriber.transcribe(node, context)
        assertEquals("![Tony](images/image.png)\n", result.content)
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
        assertEquals("![](images/image.png)\n", result.content)
    }

    @Test
    fun transcribe_emptyContent() {
        val node = MediaSingleNode(content = emptyList())
        val result = transcriber.transcribe(node, context)
        assertEquals("![](images/image.png)\n", result.content)
    }
}

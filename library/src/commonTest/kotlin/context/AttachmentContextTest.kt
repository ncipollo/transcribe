package context

import api.atlassian.Attachment
import kotlin.test.Test
import kotlin.test.assertEquals

class AttachmentContextTest {
    @Test
    fun from_createsContextWithAttachmentsAndMap() {
        val attachments = listOf(
            Attachment(
                id = "att1",
                status = "current",
                title = "file1.pdf",
                createdAt = "2024-01-01T00:00:00Z",
                mediaType = "application/pdf",
                fileSize = 1024L,
            ),
            Attachment(
                id = "att2",
                status = "current",
                title = "image.png",
                createdAt = "2024-01-02T00:00:00Z",
                mediaType = "image/png",
                fileSize = 2048L,
            ),
        )
        val expected = AttachmentContext(
            attachments = attachments,
            attachmentsById = mapOf(
                "att1" to attachments[0],
                "att2" to attachments[1],
            ),
        )

        val result = AttachmentContext.from(attachments)

        assertEquals(expected, result)
    }

    @Test
    fun from_withEmptyList_createsEmptyContext() {
        val attachments = emptyList<Attachment>()
        val expected = AttachmentContext(
            attachments = emptyList(),
            attachmentsById = emptyMap(),
        )

        val result = AttachmentContext.from(attachments)

        assertEquals(expected, result)
    }
}

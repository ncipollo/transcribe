package io.ncipollo.transcribe.context

import io.ncipollo.transcribe.api.atlassian.Attachment
import kotlin.test.Test
import kotlin.test.assertEquals

class AttachmentContextTest {
    @Test
    fun from_createsContextWithAttachmentsAndMapByFileId() {
        val attachments = listOf(
            Attachment(
                id = "att1",
                status = "current",
                title = "file1.pdf",
                createdAt = "2024-01-01T00:00:00Z",
                mediaType = "application/pdf",
                fileSize = 1024L,
                fileId = "file1",
            ),
            Attachment(
                id = "att2",
                status = "current",
                title = "image.png",
                createdAt = "2024-01-02T00:00:00Z",
                mediaType = "image/png",
                fileSize = 2048L,
                fileId = "file2",
            ),
        )
        val expected = AttachmentContext(
            attachments = attachments,
            attachmentsByFileId = mapOf(
                "file1" to attachments[0],
                "file2" to attachments[1],
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
            attachmentsByFileId = emptyMap(),
        )

        val result = AttachmentContext.from(attachments)

        assertEquals(expected, result)
    }

    @Test
    fun from_filtersOutAttachmentsWithNullFileId() {
        val attachments = listOf(
            Attachment(
                id = "att1",
                status = "current",
                title = "file1.pdf",
                createdAt = "2024-01-01T00:00:00Z",
                mediaType = "application/pdf",
                fileSize = 1024L,
                fileId = "file1",
            ),
            Attachment(
                id = "att2",
                status = "current",
                title = "no-file.txt",
                createdAt = "2024-01-02T00:00:00Z",
                mediaType = "text/plain",
                fileSize = 512L,
                fileId = null,
            ),
            Attachment(
                id = "att3",
                status = "current",
                title = "image.png",
                createdAt = "2024-01-03T00:00:00Z",
                mediaType = "image/png",
                fileSize = 2048L,
                fileId = "file3",
            ),
        )
        val expected = AttachmentContext(
            attachments = attachments,
            attachmentsByFileId = mapOf(
                "file1" to attachments[0],
                "file3" to attachments[2],
            ),
        )

        val result = AttachmentContext.from(attachments)

        assertEquals(expected, result)
    }
}

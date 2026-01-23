package io.ncipollo.transcribe.context

import io.ncipollo.transcribe.api.atlassian.Attachment

data class AttachmentContext(
    val attachments: List<Attachment> = emptyList(),
    val attachmentsByFileId: Map<String, Attachment> = emptyMap(),
) {
    companion object {
        fun from(attachments: List<Attachment>): AttachmentContext =
            AttachmentContext(
                attachments = attachments,
                attachmentsByFileId = attachments
                    .filter { it.fileId != null }
                    .associateBy { it.fileId!! },
            )
    }
}

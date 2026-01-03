package context

import api.atlassian.Attachment

data class AttachmentContext(
    val attachments: List<Attachment> = emptyList(),
    val attachmentsById: Map<String, Attachment> = emptyMap(),
) {
    companion object {
        fun from(attachments: List<Attachment>): AttachmentContext =
            AttachmentContext(
                attachments = attachments,
                attachmentsById = attachments.associateBy { it.id },
            )
    }
}

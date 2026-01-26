package io.ncipollo.transcribe.context

import io.ncipollo.transcribe.files.dropExtension
import io.ncipollo.transcribe.files.toSnakeCase

data class ADFTranscriberContext(
    val pageContext: PageContext = PageContext(),
    val attachmentContext: AttachmentContext = AttachmentContext(),
    val listLevel: Int = 0,
) {
    val suggestedDocumentName: String by lazy {
        pageContext.title.dropExtension().toSnakeCase()
    }

    val suggestedImageFolder: String by lazy {
        suggestedDocumentName.takeUnless { it.isBlank() } ?: "images"
    }
}

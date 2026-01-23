package io.ncipollo.transcribe.context

import io.ncipollo.transcribe.files.dropExtension
import io.ncipollo.transcribe.files.toSnakeCase

data class ADFTranscriberContext(
    val pageContext: PageContext = PageContext(),
    val attachmentContext: AttachmentContext = AttachmentContext(),
    val listLevel: Int = 0,
) {
    val suggestedImageFolder: String by lazy {
        pageContext.title.dropExtension().toSnakeCase().takeUnless { it.isBlank() } ?: "images"
    }
}

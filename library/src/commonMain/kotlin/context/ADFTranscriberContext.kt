package context

import files.dropExtension
import files.toSnakeCase

data class ADFTranscriberContext(
    val pageContext: PageContext = PageContext(),
    val attachmentContext: AttachmentContext = AttachmentContext(),
    val listLevel: Int = 0,
) {
    val suggestedImageFolder: String by lazy {
        pageContext.title.dropExtension().toSnakeCase().takeUnless { it.isBlank() } ?: "images"
    }
}

package context

data class ADFTranscriberContext(
    val pageContext: PageContext = PageContext(),
    val attachmentContext: AttachmentContext = AttachmentContext(),
)

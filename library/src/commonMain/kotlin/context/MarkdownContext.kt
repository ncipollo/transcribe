package context

data class MarkdownContext(
    val markdownText: String = "",
    val pageContext: PageContext = PageContext(),
    val attachmentContext: AttachmentContext = AttachmentContext(),
)

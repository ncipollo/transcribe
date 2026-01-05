import transcribe.action.AttachmentResult

data class PageMarkdownResult(
    val markdown: String = "",
    val attachmentResults: List<AttachmentResult> = emptyList(),
)

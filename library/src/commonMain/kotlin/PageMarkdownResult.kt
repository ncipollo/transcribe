import transcribe.action.AttachmentResult
import transcribe.comment.Comment

data class PageMarkdownResult(
    val markdown: String = "",
    val attachmentResults: List<AttachmentResult> = emptyList(),
    val comments: List<Comment> = emptyList(),
)

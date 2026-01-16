import transcribe.action.AttachmentResult
import transcribe.comment.CommentResult

data class PageMarkdownResult(
    val markdown: String = "",
    val attachmentResults: List<AttachmentResult> = emptyList(),
    val commentResult: CommentResult = CommentResult(),
)

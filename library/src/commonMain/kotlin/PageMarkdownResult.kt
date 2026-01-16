import transcribe.action.AttachmentResult
import transcribe.comment.CommentResult

data class PageMetadata(
    val createdAt: String? = null,
    val totalCommentCount: Int = 0,
)

data class PageMarkdownResult(
    val markdown: String = "",
    val attachmentResults: List<AttachmentResult> = emptyList(),
    val commentResult: CommentResult = CommentResult(),
    val metadata: PageMetadata = PageMetadata(),
)

package transcribe.comment

data class Comment(
    val id: String,
    val authorId: String,
    val createdAt: String,
    val content: String,
    val commentType: CommentType,
    val parentCommentId: String? = null,
    val children: List<Comment> = emptyList(),
)

data class CommentResult(
    val inlineComments: List<Comment> = emptyList(),
    val footerComments: List<Comment> = emptyList(),
)

enum class CommentType {
    INLINE,
    FOOTER,
}

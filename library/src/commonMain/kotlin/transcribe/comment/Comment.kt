package transcribe.comment

data class Comment(
    val id: String,
    val authorId: String,
    val createdAt: String,
    val content: String,
    val commentType: CommentType,
    val parentCommentId: String? = null,
)

enum class CommentType {
    INLINE,
    FOOTER,
}

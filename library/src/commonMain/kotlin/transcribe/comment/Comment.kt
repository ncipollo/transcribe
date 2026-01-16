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

/**
 * Calculates the total count of all comments, including nested children.
 */
fun CommentResult.totalCommentCount(): Int {
    return inlineComments.sumOf { it.totalCount() } +
        footerComments.sumOf { it.totalCount() }
}

private fun Comment.totalCount(): Int {
    return 1 + children.sumOf { it.totalCount() }
}

import transcribe.action.AttachmentResult
import transcribe.comment.Comment
import transcribe.comment.CommentResult
import transcribe.comment.CommentType
import kotlin.test.Test
import kotlin.test.assertEquals

class PageMarkdownResultTest {
    @Test
    fun constructor_defaultValues() {
        val result = PageMarkdownResult()

        val expected = PageMarkdownResult(
            markdown = "",
            attachmentResults = emptyList(),
            commentResult = CommentResult(),
        )
        assertEquals(expected, result)
    }

    @Test
    fun constructor_withCommentResult() {
        val inlineComment = Comment(
            id = "1",
            authorId = "author1",
            createdAt = "2024-01-01",
            content = "inline comment",
            commentType = CommentType.INLINE,
        )
        val footerComment = Comment(
            id = "2",
            authorId = "author2",
            createdAt = "2024-01-02",
            content = "footer comment",
            commentType = CommentType.FOOTER,
        )
        val commentResult = CommentResult(
            inlineComments = listOf(inlineComment),
            footerComments = listOf(footerComment),
        )

        val result = PageMarkdownResult(
            markdown = "# Test",
            commentResult = commentResult,
        )

        val expected = PageMarkdownResult(
            markdown = "# Test",
            attachmentResults = emptyList(),
            commentResult = commentResult,
        )
        assertEquals(expected, result)
    }

    @Test
    fun commentResult_accessInlineComments() {
        val inlineComment = Comment(
            id = "1",
            authorId = "author1",
            createdAt = "2024-01-01",
            content = "inline comment",
            commentType = CommentType.INLINE,
        )
        val commentResult = CommentResult(
            inlineComments = listOf(inlineComment),
        )

        val result = PageMarkdownResult(
            commentResult = commentResult,
        )

        assertEquals(listOf(inlineComment), result.commentResult.inlineComments)
    }

    @Test
    fun commentResult_accessFooterComments() {
        val footerComment = Comment(
            id = "2",
            authorId = "author2",
            createdAt = "2024-01-02",
            content = "footer comment",
            commentType = CommentType.FOOTER,
        )
        val commentResult = CommentResult(
            footerComments = listOf(footerComment),
        )

        val result = PageMarkdownResult(
            commentResult = commentResult,
        )

        assertEquals(listOf(footerComment), result.commentResult.footerComments)
    }
}

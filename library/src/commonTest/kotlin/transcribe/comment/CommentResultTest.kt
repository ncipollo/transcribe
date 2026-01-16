package transcribe.comment

import kotlin.test.Test
import kotlin.test.assertEquals

class CommentResultTest {
    @Test
    fun constructor_defaultValues() {
        val result = CommentResult()

        val expected = CommentResult(
            inlineComments = emptyList(),
            footerComments = emptyList(),
        )
        assertEquals(expected, result)
    }

    @Test
    fun constructor_withComments() {
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

        val result = CommentResult(
            inlineComments = listOf(inlineComment),
            footerComments = listOf(footerComment),
        )

        val expected = CommentResult(
            inlineComments = listOf(inlineComment),
            footerComments = listOf(footerComment),
        )
        assertEquals(expected, result)
    }
}

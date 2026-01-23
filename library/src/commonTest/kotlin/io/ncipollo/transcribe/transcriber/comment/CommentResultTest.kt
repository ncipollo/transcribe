package io.ncipollo.transcribe.transcriber.comment

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

    @Test
    fun totalCommentCount_emptyResult() {
        val result = CommentResult()

        assertEquals(0, result.totalCommentCount())
    }

    @Test
    fun totalCommentCount_withTopLevelCommentsOnly() {
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

        assertEquals(2, result.totalCommentCount())
    }

    @Test
    fun totalCommentCount_withNestedChildren() {
        val grandchild = Comment(
            id = "3",
            authorId = "author3",
            createdAt = "2024-01-03",
            content = "grandchild",
            commentType = CommentType.FOOTER,
            parentCommentId = "2",
        )
        val child = Comment(
            id = "2",
            authorId = "author2",
            createdAt = "2024-01-02",
            content = "child",
            commentType = CommentType.FOOTER,
            parentCommentId = "1",
            children = listOf(grandchild),
        )
        val parent = Comment(
            id = "1",
            authorId = "author1",
            createdAt = "2024-01-01",
            content = "parent",
            commentType = CommentType.FOOTER,
            children = listOf(child),
        )
        val result = CommentResult(
            footerComments = listOf(parent),
        )

        assertEquals(3, result.totalCommentCount())
    }
}

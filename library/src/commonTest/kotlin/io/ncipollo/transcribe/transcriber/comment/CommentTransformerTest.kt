package io.ncipollo.transcribe.transcriber.comment

import io.ncipollo.transcribe.api.atlassian.CommentBody
import io.ncipollo.transcribe.api.atlassian.CommentBodyADF
import io.ncipollo.transcribe.api.atlassian.CommentVersion
import io.ncipollo.transcribe.api.atlassian.FooterComment
import io.ncipollo.transcribe.api.atlassian.InlineComment
import io.ncipollo.transcribe.context.ADFTranscriberContext
import io.ncipollo.transcribe.context.PageContext
import io.ncipollo.transcribe.transcriber.atlassian.ConfluenceToMarkdownTranscriber
import kotlin.test.Test
import kotlin.test.assertEquals

class CommentTransformerTest {
    private val transcriber = ConfluenceToMarkdownTranscriber()
    private val transformer = CommentTransformer(transcriber)

    @Test
    fun transformFooterComment_convertsToComment() {
        val adfJson = """
            {
                "type": "doc",
                "version": 1,
                "content": [
                    {
                        "type": "paragraph",
                        "content": [
                            {
                                "type": "text",
                                "text": "This is a footer comment"
                            }
                        ]
                    }
                ]
            }
        """.trimIndent()

        val footerComment = FooterComment(
            id = "comment1",
            status = "current",
            pageId = "page123",
            parentCommentId = null,
            version = CommentVersion(
                createdAt = "2024-01-01T00:00:00Z",
                number = 1,
                minorEdit = false,
                authorId = "author1"
            ),
            body = CommentBody(
                atlasDocFormat = CommentBodyADF(value = adfJson)
            )
        )

        val context = ADFTranscriberContext(
            pageContext = PageContext(
                title = "Test Page"
            )
        )

        val result = transformer.transformFooterComment(footerComment, context)

        val expected = Comment(
            id = "comment1",
            authorId = "author1",
            createdAt = "2024-01-01T00:00:00Z",
            content = "This is a footer comment\n",
            commentType = CommentType.FOOTER,
            parentCommentId = null,
            children = emptyList(),
        )
        assertEquals(expected, result)
    }

    @Test
    fun transformFooterComment_withChildren_populatesChildren() {
        val adfJson = """
            {
                "type": "doc",
                "version": 1,
                "content": [
                    {
                        "type": "paragraph",
                        "content": [
                            {
                                "type": "text",
                                "text": "Parent comment"
                            }
                        ]
                    }
                ]
            }
        """.trimIndent()

        val footerComment = FooterComment(
            id = "parent1",
            status = "current",
            pageId = "page123",
            parentCommentId = null,
            version = CommentVersion(
                createdAt = "2024-01-01T00:00:00Z",
                number = 1,
                minorEdit = false,
                authorId = "author1"
            ),
            body = CommentBody(
                atlasDocFormat = CommentBodyADF(value = adfJson)
            )
        )

        val context = ADFTranscriberContext(
            pageContext = PageContext(
                title = "Test Page"
            )
        )

        val childComment = Comment(
            id = "child1",
            authorId = "author2",
            createdAt = "2024-01-02T00:00:00Z",
            content = "Child comment",
            commentType = CommentType.FOOTER,
            parentCommentId = "parent1",
            children = emptyList(),
        )
        val childrenMap = mapOf("parent1" to listOf(childComment))

        val result = transformer.transformFooterComment(footerComment, context, childrenMap)

        val expected = Comment(
            id = "parent1",
            authorId = "author1",
            createdAt = "2024-01-01T00:00:00Z",
            content = "Parent comment\n",
            commentType = CommentType.FOOTER,
            parentCommentId = null,
            children = listOf(childComment),
        )
        assertEquals(expected, result)
    }

    @Test
    fun transformInlineComment_convertsToComment() {
        val adfJson = """
            {
                "type": "doc",
                "version": 1,
                "content": [
                    {
                        "type": "paragraph",
                        "content": [
                            {
                                "type": "text",
                                "text": "This is an inline comment"
                            }
                        ]
                    }
                ]
            }
        """.trimIndent()

        val inlineComment = InlineComment(
            id = "comment2",
            status = "current",
            pageId = "page123",
            version = CommentVersion(
                createdAt = "2024-01-02T00:00:00Z",
                number = 1,
                minorEdit = false,
                authorId = "author2"
            ),
            body = CommentBody(
                atlasDocFormat = CommentBodyADF(value = adfJson)
            ),
            resolutionStatus = "open"
        )

        val context = ADFTranscriberContext(
            pageContext = PageContext(
                title = "Test Page"
            )
        )

        val result = transformer.transformInlineComment(inlineComment, context)

        val expected = Comment(
            id = "comment2",
            authorId = "author2",
            createdAt = "2024-01-02T00:00:00Z",
            content = "This is an inline comment\n",
            commentType = CommentType.INLINE,
            parentCommentId = null,
            children = emptyList(),
        )
        assertEquals(expected, result)
    }

    @Test
    fun transformInlineComment_withChildren_populatesChildren() {
        val adfJson = """
            {
                "type": "doc",
                "version": 1,
                "content": [
                    {
                        "type": "paragraph",
                        "content": [
                            {
                                "type": "text",
                                "text": "Parent inline comment"
                            }
                        ]
                    }
                ]
            }
        """.trimIndent()

        val inlineComment = InlineComment(
            id = "parent2",
            status = "current",
            pageId = "page123",
            version = CommentVersion(
                createdAt = "2024-01-02T00:00:00Z",
                number = 1,
                minorEdit = false,
                authorId = "author2"
            ),
            body = CommentBody(
                atlasDocFormat = CommentBodyADF(value = adfJson)
            ),
            resolutionStatus = "open"
        )

        val context = ADFTranscriberContext(
            pageContext = PageContext(
                title = "Test Page"
            )
        )

        val childComment = Comment(
            id = "child2",
            authorId = "author3",
            createdAt = "2024-01-03T00:00:00Z",
            content = "Child inline comment",
            commentType = CommentType.INLINE,
            parentCommentId = null,
            children = emptyList(),
        )
        val childrenMap = mapOf("parent2" to listOf(childComment))

        val result = transformer.transformInlineComment(inlineComment, context, childrenMap)

        val expected = Comment(
            id = "parent2",
            authorId = "author2",
            createdAt = "2024-01-02T00:00:00Z",
            content = "Parent inline comment\n",
            commentType = CommentType.INLINE,
            parentCommentId = null,
            children = listOf(childComment),
        )
        assertEquals(expected, result)
    }
}

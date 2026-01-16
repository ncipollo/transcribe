package transcribe.comment

import api.atlassian.CommentBody
import api.atlassian.CommentBodyADF
import api.atlassian.CommentVersion
import api.atlassian.FooterComment
import api.atlassian.InlineComment
import context.ADFTranscriberContext
import context.PageContext
import transcribe.atlassian.ConfluenceToMarkdownTranscriber
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
        )
        assertEquals(expected, result)
    }
}

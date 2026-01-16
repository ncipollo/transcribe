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

class CommentChildrenMapperTest {
    private val transcriber = ConfluenceToMarkdownTranscriber()
    private val transformer = CommentTransformer(transcriber)
    private val context = ADFTranscriberContext(
        pageContext = PageContext(title = "Test Page")
    )

    private val simpleAdfJson = """
        {
            "type": "doc",
            "version": 1,
            "content": [
                {
                    "type": "paragraph",
                    "content": [
                        {
                            "type": "text",
                            "text": "Test comment"
                        }
                    ]
                }
            ]
        }
    """.trimIndent()

    @Test
    fun buildChildrenMap_emptyInputs_returnsEmptyMap() {
        val result = CommentChildrenMapper.buildChildrenMap(
            footerChildrenByParent = emptyMap(),
            inlineChildrenByParent = emptyMap(),
            transformer = transformer,
            context = context,
        )

        assertEquals(emptyMap(), result)
    }

    @Test
    fun buildChildrenMap_withFooterChildren_mapsCorrectly() {
        val childFooterComment = FooterComment(
            id = "child1",
            status = "current",
            pageId = "page123",
            parentCommentId = "parent1",
            version = CommentVersion(
                createdAt = "2024-01-02T00:00:00Z",
                number = 1,
                minorEdit = false,
                authorId = "author2"
            ),
            body = CommentBody(atlasDocFormat = CommentBodyADF(value = simpleAdfJson))
        )

        val footerChildrenByParent = mapOf("parent1" to listOf(childFooterComment))

        val result = CommentChildrenMapper.buildChildrenMap(
            footerChildrenByParent = footerChildrenByParent,
            inlineChildrenByParent = emptyMap(),
            transformer = transformer,
            context = context,
        )

        val expectedChild = Comment(
            id = "child1",
            authorId = "author2",
            createdAt = "2024-01-02T00:00:00Z",
            content = "Test comment\n",
            commentType = CommentType.FOOTER,
            parentCommentId = "parent1",
            children = emptyList(),
        )
        val expected = mapOf("parent1" to listOf(expectedChild))
        assertEquals(expected, result)
    }

    @Test
    fun buildChildrenMap_withInlineChildren_mapsCorrectly() {
        val childInlineComment = InlineComment(
            id = "child2",
            status = "current",
            pageId = "page123",
            version = CommentVersion(
                createdAt = "2024-01-03T00:00:00Z",
                number = 1,
                minorEdit = false,
                authorId = "author3"
            ),
            body = CommentBody(atlasDocFormat = CommentBodyADF(value = simpleAdfJson)),
            resolutionStatus = "open"
        )

        val inlineChildrenByParent = mapOf("parent2" to listOf(childInlineComment))

        val result = CommentChildrenMapper.buildChildrenMap(
            footerChildrenByParent = emptyMap(),
            inlineChildrenByParent = inlineChildrenByParent,
            transformer = transformer,
            context = context,
        )

        val expectedChild = Comment(
            id = "child2",
            authorId = "author3",
            createdAt = "2024-01-03T00:00:00Z",
            content = "Test comment\n",
            commentType = CommentType.INLINE,
            parentCommentId = null,
            children = emptyList(),
        )
        val expected = mapOf("parent2" to listOf(expectedChild))
        assertEquals(expected, result)
    }

    @Test
    fun buildChildrenMap_withMixedChildren_combinesBothTypes() {
        val childFooterComment = FooterComment(
            id = "child1",
            status = "current",
            pageId = "page123",
            parentCommentId = "parent1",
            version = CommentVersion(
                createdAt = "2024-01-02T00:00:00Z",
                number = 1,
                minorEdit = false,
                authorId = "author2"
            ),
            body = CommentBody(atlasDocFormat = CommentBodyADF(value = simpleAdfJson))
        )

        val childInlineComment = InlineComment(
            id = "child2",
            status = "current",
            pageId = "page123",
            version = CommentVersion(
                createdAt = "2024-01-03T00:00:00Z",
                number = 1,
                minorEdit = false,
                authorId = "author3"
            ),
            body = CommentBody(atlasDocFormat = CommentBodyADF(value = simpleAdfJson)),
            resolutionStatus = "open"
        )

        val footerChildrenByParent = mapOf("parent1" to listOf(childFooterComment))
        val inlineChildrenByParent = mapOf("parent2" to listOf(childInlineComment))

        val result = CommentChildrenMapper.buildChildrenMap(
            footerChildrenByParent = footerChildrenByParent,
            inlineChildrenByParent = inlineChildrenByParent,
            transformer = transformer,
            context = context,
        )

        val expectedFooterChild = Comment(
            id = "child1",
            authorId = "author2",
            createdAt = "2024-01-02T00:00:00Z",
            content = "Test comment\n",
            commentType = CommentType.FOOTER,
            parentCommentId = "parent1",
            children = emptyList(),
        )
        val expectedInlineChild = Comment(
            id = "child2",
            authorId = "author3",
            createdAt = "2024-01-03T00:00:00Z",
            content = "Test comment\n",
            commentType = CommentType.INLINE,
            parentCommentId = null,
            children = emptyList(),
        )
        val expected = mapOf(
            "parent1" to listOf(expectedFooterChild),
            "parent2" to listOf(expectedInlineChild),
        )
        assertEquals(expected, result)
    }
}

package io.ncipollo.transcribe.feature

import io.ncipollo.transcribe.api.atlassian.AtlasDocFormat
import io.ncipollo.transcribe.api.atlassian.AttachmentAPIClient
import io.ncipollo.transcribe.api.atlassian.CommentAPIClient
import io.ncipollo.transcribe.api.atlassian.PageAPIClient
import io.ncipollo.transcribe.api.atlassian.PageBody
import io.ncipollo.transcribe.api.atlassian.PageResponse
import io.ncipollo.transcribe.api.atlassian.PageVersion
import io.ncipollo.transcribe.context.ADFTranscriberContext
import io.ncipollo.transcribe.data.atlassian.adf.DocNode
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import io.ncipollo.transcribe.transcriber.TranscribeResult
import io.ncipollo.transcribe.transcriber.action.ActionHandler
import io.ncipollo.transcribe.transcriber.atlassian.ConfluenceToMarkdownTranscriber
import io.ncipollo.transcribe.transcriber.comment.CommentTransformer
import io.ncipollo.transcribe.transcriber.transformer.ADFTransformer
import kotlin.test.Test
import kotlin.test.assertEquals

class PageMarkdownFetchFeatureTest {
    private val pageApiClient = mockk<PageAPIClient>()
    private val attachmentApiClient = mockk<AttachmentAPIClient>()
    private val commentApiClient = mockk<CommentAPIClient>()
    private val transcriber = mockk<ConfluenceToMarkdownTranscriber>()
    private val commentTransformer = mockk<CommentTransformer>()
    private val actionHandler = mockk<ActionHandler>()
    private val toMarkdownTransformer = mockk<ADFTransformer<ADFTranscriberContext>>()

    private val feature = PageMarkdownFetchFeature(
        pageApiClient = pageApiClient,
        attachmentApiClient = attachmentApiClient,
        commentApiClient = commentApiClient,
        transcriber = transcriber,
        commentTransformer = commentTransformer,
        actionHandler = actionHandler,
        toMarkdownTransformer = toMarkdownTransformer,
    )

    @Test
    fun fetch_transcribesPageToMarkdown() = runBlocking {
        val pageId = "page-123"
        val adfJson = """{"type":"doc","version":1,"content":[]}"""

        val page = PageResponse(
            id = pageId,
            status = "current",
            title = "Test Page",
            spaceId = "space-1",
            authorId = "author-1",
            createdAt = "2024-01-01T00:00:00Z",
            version = PageVersion(
                createdAt = "2024-01-01T00:00:00Z",
                number = 1,
                minorEdit = false,
                authorId = "author-1",
            ),
            body = PageBody(
                atlasDocFormat = AtlasDocFormat(value = adfJson),
            ),
        )

        val transcribeResult = TranscribeResult(content = "# Test", actions = emptyList())

        coEvery { pageApiClient.getPage(pageId) } returns page
        coEvery { attachmentApiClient.getPageAttachments(pageId) } returns emptyList()
        coEvery { commentApiClient.getPageFooterComments(pageId) } returns emptyList()
        coEvery { commentApiClient.getPageInlineComments(pageId) } returns emptyList()
        every { toMarkdownTransformer.transform(any(), any()) } returns emptyList()
        every { transcriber.transcribe(any<DocNode>(), any()) } returns transcribeResult
        coEvery { actionHandler.handleActions(emptyList()) } returns emptyList()

        val result = feature.fetch(pageId)

        assertEquals("# Test", result.markdown)
        assertEquals(emptyList(), result.attachmentResults)
        assertEquals("2024-01-01T00:00:00Z", result.metadata.createdAt)
        assertEquals(0, result.metadata.totalCommentCount)

        coVerify { pageApiClient.getPage(pageId) }
        coVerify { attachmentApiClient.getPageAttachments(pageId) }
        coVerify { commentApiClient.getPageFooterComments(pageId) }
        coVerify { commentApiClient.getPageInlineComments(pageId) }
    }

    @Test
    fun fetch_withNoAdfBody_throwsException() = runBlocking {
        val pageId = "page-123"

        val page = PageResponse(
            id = pageId,
            status = "current",
            title = "Test Page",
            spaceId = "space-1",
            authorId = "author-1",
            createdAt = "2024-01-01T00:00:00Z",
            version = PageVersion(
                createdAt = "2024-01-01T00:00:00Z",
                number = 1,
                minorEdit = false,
                authorId = "author-1",
            ),
            body = null,
        )

        coEvery { pageApiClient.getPage(pageId) } returns page
        coEvery { attachmentApiClient.getPageAttachments(pageId) } returns emptyList()
        coEvery { commentApiClient.getPageFooterComments(pageId) } returns emptyList()
        coEvery { commentApiClient.getPageInlineComments(pageId) } returns emptyList()

        val exception = try {
            feature.fetch(pageId)
            null
        } catch (e: IllegalStateException) {
            e
        }

        assertEquals("Page $pageId does not contain ADF body content", exception?.message)
    }
}

package feature

import api.atlassian.AttachmentAPIClient
import api.atlassian.PageAPIClient
import api.atlassian.PageBody
import api.atlassian.PageResponse
import api.atlassian.PageVersion
import context.MarkdownContext
import data.atlassian.adf.DocNode
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import transcribe.TranscribeResult
import transcribe.markdown.MarkdownToConfluenceTranscriber
import transcribe.transformer.ADFTransformer
import kotlin.test.Test
import kotlin.test.assertEquals

class PageMarkdownUpdateFeatureTest {
    private val pageApiClient = mockk<PageAPIClient>()
    private val attachmentApiClient = mockk<AttachmentAPIClient>()
    private val markdownTranscriber = mockk<MarkdownToConfluenceTranscriber>()
    private val toConfluenceTransformer = mockk<ADFTransformer<MarkdownContext>>()

    private val feature = PageMarkdownUpdateFeature(
        pageApiClient = pageApiClient,
        attachmentApiClient = attachmentApiClient,
        markdownTranscriber = markdownTranscriber,
        toConfluenceTransformer = toConfluenceTransformer,
    )

    @Test
    fun update_transcribesMarkdownAndUpdatesPage() = runBlocking {
        val pageId = "page-123"
        val markdown = "# Test"
        val message = "Update message"

        val currentPage = PageResponse(
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

        val docNode = DocNode(content = emptyList())
        val transcribeResult = TranscribeResult(content = docNode)

        val updatedPage = currentPage.copy(
            version = currentPage.version.copy(number = 2),
        )

        coEvery { pageApiClient.getPage(pageId) } returns currentPage
        coEvery { attachmentApiClient.getPageAttachments(pageId) } returns emptyList()
        coEvery { markdownTranscriber.transcribe(markdown, any()) } returns transcribeResult
        coEvery { toConfluenceTransformer.transform(any(), any()) } returns emptyList()
        coEvery {
            pageApiClient.updatePage(
                pageId = pageId,
                title = currentPage.title,
                docNode = any(),
                version = 2,
                status = currentPage.status,
                message = message,
            )
        } returns updatedPage

        val result = feature.update(pageId, markdown, message)

        assertEquals(updatedPage, result)
        coVerify { pageApiClient.getPage(pageId) }
        coVerify { attachmentApiClient.getPageAttachments(pageId) }
        coVerify { markdownTranscriber.transcribe(markdown, any()) }
    }

    @Test
    fun update_withoutMessage_updatesPageWithNullMessage() = runBlocking {
        val pageId = "page-123"
        val markdown = "# Test"

        val currentPage = PageResponse(
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

        val docNode = DocNode(content = emptyList())
        val transcribeResult = TranscribeResult(content = docNode)

        coEvery { pageApiClient.getPage(pageId) } returns currentPage
        coEvery { attachmentApiClient.getPageAttachments(pageId) } returns emptyList()
        coEvery { markdownTranscriber.transcribe(markdown, any()) } returns transcribeResult
        coEvery { toConfluenceTransformer.transform(any(), any()) } returns emptyList()
        coEvery {
            pageApiClient.updatePage(
                pageId = pageId,
                title = currentPage.title,
                docNode = any(),
                version = 2,
                status = currentPage.status,
                message = null,
            )
        } returns currentPage

        feature.update(pageId, markdown)

        coVerify {
            pageApiClient.updatePage(
                pageId = pageId,
                title = currentPage.title,
                docNode = any(),
                version = 2,
                status = currentPage.status,
                message = null,
            )
        }
    }
}

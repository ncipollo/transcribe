import api.atlassian.PageResponse
import api.atlassian.PageVersion
import api.atlassian.TemplateResponse
import api.atlassian.TemplateSpace
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import transcribe.comment.CommentResult
import kotlin.test.Test
import kotlin.test.assertEquals

class JvmTranscribeTest {
    private val mockTranscribe = mockk<Transcribe>()

    private val jvmTranscribe = JvmTranscribe(
        transcribe = mockTranscribe,
    )

    @Test
    fun getPageMarkdown_delegatesToTranscribe() {
        val url = "https://example.atlassian.net/wiki/spaces/TEST/pages/123/Test"
        val expectedResult = PageMarkdownResult(
            markdown = "# Test",
            commentResult = CommentResult(),
        )

        coEvery { mockTranscribe.getPageMarkdown(url) } returns expectedResult

        val result = jvmTranscribe.getPageMarkdown(url)

        assertEquals(expectedResult, result)
        coVerify { mockTranscribe.getPageMarkdown(url) }
    }

    @Test
    fun updatePageMarkdown_delegatesToTranscribe() {
        val url = "https://example.atlassian.net/wiki/spaces/TEST/pages/123/Test"
        val markdown = "# Updated"
        val message = "Update message"
        val expectedResponse = PageResponse(
            id = "123",
            status = "current",
            title = "Test",
            spaceId = "space-1",
            authorId = "author-1",
            createdAt = "2024-01-01T00:00:00Z",
            version = PageVersion(
                createdAt = "2024-01-01T00:00:00Z",
                number = 2,
                minorEdit = false,
                authorId = "author-1",
            ),
        )

        coEvery { mockTranscribe.updatePageMarkdown(url, markdown, message) } returns expectedResponse

        val result = jvmTranscribe.updatePageMarkdown(url, markdown, message)

        assertEquals(expectedResponse, result)
        coVerify { mockTranscribe.updatePageMarkdown(url, markdown, message) }
    }

    @Test
    fun updatePageMarkdown_withNullMessage_delegatesToTranscribe() {
        val url = "https://example.atlassian.net/wiki/spaces/TEST/pages/123/Test"
        val markdown = "# Updated"
        val expectedResponse = PageResponse(
            id = "123",
            status = "current",
            title = "Test",
            spaceId = "space-1",
            authorId = "author-1",
            createdAt = "2024-01-01T00:00:00Z",
            version = PageVersion(
                createdAt = "2024-01-01T00:00:00Z",
                number = 2,
                minorEdit = false,
                authorId = "author-1",
            ),
        )

        coEvery { mockTranscribe.updatePageMarkdown(url, markdown, null) } returns expectedResponse

        val result = jvmTranscribe.updatePageMarkdown(url, markdown)

        assertEquals(expectedResponse, result)
        coVerify { mockTranscribe.updatePageMarkdown(url, markdown, null) }
    }

    @Test
    fun updateTemplateMarkdown_delegatesToTranscribe() {
        val templateId = "template-123"
        val markdown = "# Template"
        val name = "Test Template"
        val templateType = "page"
        val expectedResponse = TemplateResponse(
            templateId = templateId,
            name = name,
            description = "Description",
            labels = emptyList(),
            templateType = templateType,
            body = null,
            space = TemplateSpace(key = "TEST"),
        )

        coEvery {
            mockTranscribe.updateTemplateMarkdown(templateId, markdown, name, templateType)
        } returns expectedResponse

        val result = jvmTranscribe.updateTemplateMarkdown(templateId, markdown, name, templateType)

        assertEquals(expectedResponse, result)
        coVerify { mockTranscribe.updateTemplateMarkdown(templateId, markdown, name, templateType) }
    }

    @Test
    fun close_delegatesToTranscribe() {
        every { mockTranscribe.close() } just runs

        jvmTranscribe.close()

        verify { mockTranscribe.close() }
    }
}

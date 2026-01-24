package io.ncipollo.transcribe

import io.ncipollo.transcribe.api.atlassian.PageResponse
import io.ncipollo.transcribe.api.atlassian.PageVersion
import io.ncipollo.transcribe.api.atlassian.TemplateResponse
import io.ncipollo.transcribe.api.atlassian.TemplateSpace
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import io.ncipollo.transcribe.transcriber.comment.CommentResult
import kotlin.test.Test
import kotlin.test.assertEquals

class JvmTranscribeTest {
    private val mockTranscribe = mockk<Transcribe>()
    private val mockConfiguration = mockk<TranscribeConfiguration>()

    private val jvmTranscribe = JvmTranscribe(
        configuration = mockConfiguration,
    ).apply {
        // Use reflection to inject the mock
        val transcribeField = JvmTranscribe::class.java.getDeclaredField("transcribe")
        transcribeField.isAccessible = true
        transcribeField.set(this, mockTranscribe)
    }

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
    fun getPageMarkdownByPageId_delegatesToTranscribe() {
        val pageId = "123"
        val expectedResult = PageMarkdownResult(
            markdown = "# Test",
            commentResult = CommentResult(),
        )

        coEvery { mockTranscribe.getPageMarkdownByPageId(pageId) } returns expectedResult

        val result = jvmTranscribe.getPageMarkdownByPageId(pageId)

        assertEquals(expectedResult, result)
        coVerify { mockTranscribe.getPageMarkdownByPageId(pageId) }
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
    fun updatePageMarkdownByPageId_delegatesToTranscribe() {
        val pageId = "123"
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

        coEvery { mockTranscribe.updatePageMarkdownByPageId(pageId, markdown, message) } returns expectedResponse

        val result = jvmTranscribe.updatePageMarkdownByPageId(pageId, markdown, message)

        assertEquals(expectedResponse, result)
        coVerify { mockTranscribe.updatePageMarkdownByPageId(pageId, markdown, message) }
    }

    @Test
    fun updatePageMarkdownByPageId_withNullMessage_delegatesToTranscribe() {
        val pageId = "123"
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

        coEvery { mockTranscribe.updatePageMarkdownByPageId(pageId, markdown, null) } returns expectedResponse

        val result = jvmTranscribe.updatePageMarkdownByPageId(pageId, markdown)

        assertEquals(expectedResponse, result)
        coVerify { mockTranscribe.updatePageMarkdownByPageId(pageId, markdown, null) }
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

package context

import api.atlassian.PageResponse
import api.atlassian.PageVersion
import api.atlassian.TemplateResponse
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class PageContextTest {
    @Test
    fun fromPageResponse_extractsTitleAndCreatedAt() {
        val pageResponse = PageResponse(
            id = "123",
            status = "current",
            title = "Test Page",
            spaceId = "SPACE",
            authorId = "user123",
            createdAt = "2024-01-01T00:00:00Z",
            version = PageVersion(
                createdAt = "2024-01-01T00:00:00Z",
                number = 1,
                minorEdit = false,
                authorId = "user123",
            ),
        )

        val result = PageContext.fromPageResponse(pageResponse)

        assertEquals("Test Page", result.title)
        assertEquals("2024-01-01T00:00:00Z", result.createdAt)
    }

    @Test
    fun fromTemplateResponse_extractsNameAsTitle() {
        val templateResponse = TemplateResponse(
            templateId = "template123",
            name = "Test Template",
            templateType = "page",
        )

        val result = PageContext.fromTemplateResponse(templateResponse)

        assertEquals("Test Template", result.title)
        assertNull(result.createdAt)
    }

    @Test
    fun fromTemplateResponse_setsCreatedAtToNull() {
        val templateResponse = TemplateResponse(
            templateId = "template456",
            name = "Another Template",
            templateType = "page",
        )

        val result = PageContext.fromTemplateResponse(templateResponse)

        assertEquals("Another Template", result.title)
        assertNull(result.createdAt)
    }
}

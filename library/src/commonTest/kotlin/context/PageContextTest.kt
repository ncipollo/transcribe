package context

import api.atlassian.PageResponse
import api.atlassian.PageVersion
import api.atlassian.TemplateResponse
import kotlin.test.Test
import kotlin.test.assertEquals

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
        val expected = PageContext(
            title = "Test Page",
            createdAt = "2024-01-01T00:00:00Z",
        )

        val result = PageContext.fromPageResponse(pageResponse)

        assertEquals(expected, result)
    }

    @Test
    fun fromTemplateResponse_extractsNameAsTitleWithNullCreatedAt() {
        val templateResponse = TemplateResponse(
            templateId = "template123",
            name = "Test Template",
            templateType = "page",
        )
        val expected = PageContext(
            title = "Test Template",
            createdAt = null,
        )

        val result = PageContext.fromTemplateResponse(templateResponse)

        assertEquals(expected, result)
    }
}

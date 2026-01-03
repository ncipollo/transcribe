package context

import api.atlassian.PageResponse
import api.atlassian.TemplateResponse

data class PageContext(
    val title: String = "",
    val createdAt: String? = null,
) {
    companion object {
        fun fromPageResponse(response: PageResponse): PageContext =
            PageContext(
                title = response.title,
                createdAt = response.createdAt,
            )

        fun fromTemplateResponse(response: TemplateResponse): PageContext =
            PageContext(
                title = response.name,
                createdAt = null,
            )
    }
}

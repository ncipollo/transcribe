package api.atlassian

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*

class PageAPIClient(private val httpClient: HttpClient) {
    suspend fun getPage(pageId: String): PageResponse {
        return httpClient.get("pages/$pageId") {
            parameter("body-format", "atlas_doc_format")
        }.body()
    }
}


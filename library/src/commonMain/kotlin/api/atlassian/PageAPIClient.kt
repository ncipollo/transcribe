package api.atlassian

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class PageAPIClient(private val httpClient: HttpClient) {
    suspend fun getPage(pageId: String): PageResponse {
        return httpClient.get("pages/$pageId") {
            parameter("body-format", "atlas_doc_format")
        }.body()
    }
}

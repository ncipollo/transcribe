package api.atlassian

import data.atlassian.adf.ADFSerializer
import data.atlassian.adf.DocNode
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

class PageAPIClient(private val httpClient: HttpClient) {
    suspend fun getPage(pageId: String): PageResponse {
        return httpClient.get("pages/$pageId") {
            parameter("body-format", "atlas_doc_format")
        }.body()
    }

    suspend fun updatePage(
        pageId: String,
        title: String,
        docNode: DocNode,
        version: Int,
        status: String = "current",
        message: String? = null,
    ): PageResponse {
        val adfJson = ADFSerializer.toJson(docNode)
        val request = PageUpdateRequest(
            id = pageId,
            status = status,
            title = title,
            version = PageUpdateVersion(
                number = version,
                message = message,
            ),
            body = PageUpdateBody(
                representation = "atlas_doc_format",
                value = adfJson,
            ),
        )
        return httpClient.put("pages/$pageId") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
    }
}

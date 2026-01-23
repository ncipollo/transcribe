package io.ncipollo.transcribe.api.atlassian

import io.ncipollo.transcribe.data.atlassian.adf.ADFSerializer
import io.ncipollo.transcribe.data.atlassian.adf.DocNode
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess

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
        val request =
            PageUpdateRequest(
                id = pageId,
                status = status,
                title = title,
                version =
                PageUpdateVersion(
                    number = version,
                    message = message,
                ),
                body =
                PageUpdateBody(
                    representation = "atlas_doc_format",
                    value = adfJson,
                ),
            )
        val response =
            httpClient.put("pages/$pageId") {
                contentType(ContentType.Application.Json)
                setBody(request)
            }

        if (!response.status.isSuccess()) {
            val errorBody = response.bodyAsText()
            throw ConfluenceApiException(
                statusCode = response.status.value,
                errorBody = errorBody,
                message = "Failed to update page $pageId: ${response.status} - $errorBody",
            )
        }

        return response.body()
    }
}

package io.ncipollo.transcribe.api.atlassian

import io.ncipollo.transcribe.data.atlassian.adf.ADFSerializer
import io.ncipollo.transcribe.data.atlassian.adf.DocNode
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess

class TemplateAPIClient(private val httpClient: HttpClient) {
    suspend fun getTemplate(templateId: String): TemplateResponse {
        return httpClient.get("template/$templateId") {
            contentType(ContentType.Application.Json)
        }.body()
    }

    suspend fun updateTemplate(
        templateId: String,
        name: String,
        docNode: DocNode,
        templateType: String = "page",
        description: String? = null,
        labels: List<TemplateLabel>? = null,
        space: TemplateSpace? = null,
    ): TemplateResponse {
        val adfJson = ADFSerializer.toJson(docNode)
        val request =
            TemplateUpdateRequest(
                templateId = templateId,
                name = name,
                templateType = templateType,
                body =
                TemplateUpdateBody(
                    atlasDocFormat =
                    TemplateBodyValue(
                        value = adfJson,
                        representation = "atlas_doc_format",
                    ),
                ),
                description = description,
                labels = labels,
                space = space,
            )
        val response =
            httpClient.put("template") {
                contentType(ContentType.Application.Json)
                setBody(request)
            }

        if (!response.status.isSuccess()) {
            val errorBody = response.bodyAsText()
            throw ConfluenceApiException(
                statusCode = response.status.value,
                errorBody = errorBody,
                message = "Failed to update template $templateId: ${response.status} - $errorBody",
            )
        }

        return response.body()
    }
}

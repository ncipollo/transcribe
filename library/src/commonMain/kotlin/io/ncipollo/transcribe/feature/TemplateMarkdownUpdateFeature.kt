package io.ncipollo.transcribe.feature

import io.ncipollo.transcribe.api.atlassian.TemplateAPIClient
import io.ncipollo.transcribe.api.atlassian.TemplateResponse
import io.ncipollo.transcribe.context.MarkdownContext
import io.ncipollo.transcribe.context.PageContext
import io.ncipollo.transcribe.transcriber.markdown.MarkdownToConfluenceTranscriber
import io.ncipollo.transcribe.transcriber.transformer.ADFTransformer

/**
 * Feature for updating a Confluence template with Markdown content.
 */
class TemplateMarkdownUpdateFeature(
    private val templateApiClient: TemplateAPIClient,
    private val markdownTranscriber: MarkdownToConfluenceTranscriber,
    private val toConfluenceTransformer: ADFTransformer<MarkdownContext>,
) {
    /**
     * Updates a Confluence template with markdown content.
     * Converts the markdown to ADF format and updates the template.
     *
     * @param templateId The template ID to update
     * @param markdown The markdown content to update the template with
     * @param name The template name (required for update)
     * @param templateType The template type (defaults to "page")
     * @return The updated template response
     * @throws IllegalStateException if markdown transcription fails or template cannot be updated
     */
    suspend fun update(
        templateId: String,
        markdown: String,
        name: String,
        templateType: String = "page",
    ): TemplateResponse {
        val currentTemplate = templateApiClient.getTemplate(templateId)

        val context = MarkdownContext(
            markdownText = markdown,
            pageContext = PageContext.fromTemplateResponse(currentTemplate),
        )
        val result = markdownTranscriber.transcribe(markdown, context)
        val docNode = result.content

        // Apply toConfluence transformer after transcribing
        val transformedContent = toConfluenceTransformer.transform(docNode.content, context)
        val transformedDocNode = docNode.copy(content = transformedContent)

        return templateApiClient.updateTemplate(
            templateId = templateId,
            name = name,
            docNode = transformedDocNode,
            templateType = templateType,
            description = currentTemplate.description,
            labels = currentTemplate.labels,
            space = currentTemplate.space,
        )
    }
}

package io.ncipollo.transcribe.feature

import io.ncipollo.transcribe.api.atlassian.AttachmentAPIClient
import io.ncipollo.transcribe.api.atlassian.PageAPIClient
import io.ncipollo.transcribe.api.atlassian.PageResponse
import io.ncipollo.transcribe.context.AttachmentContext
import io.ncipollo.transcribe.context.MarkdownContext
import io.ncipollo.transcribe.context.PageContext
import io.ncipollo.transcribe.transcriber.markdown.MarkdownToConfluenceTranscriber
import io.ncipollo.transcribe.transcriber.transformer.ADFTransformer

/**
 * Feature for updating a Confluence page with Markdown content.
 */
class PageMarkdownUpdateFeature(
    private val pageApiClient: PageAPIClient,
    private val attachmentApiClient: AttachmentAPIClient,
    private val markdownTranscriber: MarkdownToConfluenceTranscriber,
    private val toConfluenceTransformer: ADFTransformer<MarkdownContext>,
) {
    /**
     * Updates a Confluence page with markdown content.
     * Converts the markdown to ADF format and updates the page.
     *
     * @param pageId The Confluence page ID
     * @param markdown The markdown content to update the page with
     * @param message Optional version message for the update
     * @return The updated page response
     * @throws IllegalStateException if markdown transcription fails or page cannot be updated
     */
    suspend fun update(
        pageId: String,
        markdown: String,
        message: String? = null,
    ): PageResponse {
        val currentPage = pageApiClient.getPage(pageId)
        val attachments = attachmentApiClient.getPageAttachments(pageId)

        val context = MarkdownContext(
            markdownText = markdown,
            pageContext = PageContext.fromPageResponse(currentPage),
            attachmentContext = AttachmentContext.from(attachments),
        )
        val result = markdownTranscriber.transcribe(markdown, context)
        val docNode = result.content

        // Apply toConfluence transformer after transcribing
        val transformedContent = toConfluenceTransformer.transform(docNode.content, context)
        val transformedDocNode = docNode.copy(content = transformedContent)

        return pageApiClient.updatePage(
            pageId = pageId,
            title = currentPage.title,
            docNode = transformedDocNode,
            version = currentPage.version.number + 1,
            status = currentPage.status,
            message = message,
        )
    }
}

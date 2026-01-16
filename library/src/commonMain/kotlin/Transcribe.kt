import api.atlassian.AttachmentAPIClient
import api.atlassian.CommentAPIClient
import api.atlassian.ConfluenceHttpClientFactory
import api.atlassian.ConfluenceUrlParser
import api.atlassian.PageAPIClient
import api.atlassian.PageResponse
import api.atlassian.TemplateAPIClient
import api.atlassian.TemplateResponse
import feature.PageMarkdownFetchFeature
import feature.PageMarkdownUpdateFeature
import feature.TemplateMarkdownUpdateFeature
import io.ktor.client.HttpClient
import transcribe.atlassian.ConfluenceToMarkdownTranscriber
import transcribe.comment.CommentTransformer
import transcribe.markdown.MarkdownToConfluenceTranscriber

/**
 * Main entry point for transcribing Confluence pages to Markdown.
 * Provides methods to fetch and transcribe Confluence pages.
 */
class Transcribe(
    private val configuration: TranscribeConfiguration = TranscribeConfiguration(),
) {
    private val httpClient: HttpClient by lazy {
        ConfluenceHttpClientFactory.create(
            siteName = configuration.confluenceConfiguration.siteName,
            authMaterial = configuration.confluenceConfiguration.authMaterial,
        )
    }

    private val httpClientV1: HttpClient by lazy {
        ConfluenceHttpClientFactory.create(
            siteName = configuration.confluenceConfiguration.siteName,
            authMaterial = configuration.confluenceConfiguration.authMaterial,
            apiVersion = "v1",
        )
    }

    private val pageApiClient: PageAPIClient by lazy {
        PageAPIClient(httpClient)
    }

    private val attachmentApiClient: AttachmentAPIClient by lazy {
        AttachmentAPIClient(httpClient)
    }

    private val commentApiClient: CommentAPIClient by lazy {
        CommentAPIClient(httpClient)
    }

    private val templateApiClient: TemplateAPIClient by lazy {
        TemplateAPIClient(httpClientV1)
    }

    private val transcriber: ConfluenceToMarkdownTranscriber by lazy {
        ConfluenceToMarkdownTranscriber(
            customTranscribers = configuration.adfCustomTranscribers,
        )
    }

    private val markdownTranscriber: MarkdownToConfluenceTranscriber by lazy {
        MarkdownToConfluenceTranscriber(
            customTranscribers = configuration.markdownCustomTranscribers,
        )
    }

    private val commentTransformer: CommentTransformer by lazy {
        CommentTransformer(transcriber)
    }

    private val actionHandler: transcribe.action.ActionHandler by lazy {
        transcribe.action.ActionHandler(attachmentApiClient)
    }

    private val pageMarkdownFetchFeature: PageMarkdownFetchFeature by lazy {
        PageMarkdownFetchFeature(
            pageApiClient = pageApiClient,
            attachmentApiClient = attachmentApiClient,
            commentApiClient = commentApiClient,
            transcriber = transcriber,
            commentTransformer = commentTransformer,
            actionHandler = actionHandler,
            toMarkdownTransformer = configuration.toMarkdownTransformer,
        )
    }

    private val pageMarkdownUpdateFeature: PageMarkdownUpdateFeature by lazy {
        PageMarkdownUpdateFeature(
            pageApiClient = pageApiClient,
            attachmentApiClient = attachmentApiClient,
            markdownTranscriber = markdownTranscriber,
            toConfluenceTransformer = configuration.toConfluenceTransformer,
        )
    }

    private val templateMarkdownUpdateFeature: TemplateMarkdownUpdateFeature by lazy {
        TemplateMarkdownUpdateFeature(
            templateApiClient = templateApiClient,
            markdownTranscriber = markdownTranscriber,
            toConfluenceTransformer = configuration.toConfluenceTransformer,
        )
    }

    /**
     * Fetches a Confluence page by URL and returns its content as Markdown.
     *
     * @param url The full Confluence page URL
     * @return PageMarkdownResult containing the markdown content and any attachment results
     * @throws IllegalArgumentException if the URL cannot be parsed to extract a page ID
     * @throws Exception if the page cannot be fetched or transcribed
     */
    suspend fun getPageMarkdown(url: String): PageMarkdownResult {
        val pageId =
            ConfluenceUrlParser.extractPageId(url)
                ?: throw IllegalArgumentException("Unable to extract page ID from URL: $url")

        return pageMarkdownFetchFeature.fetch(pageId)
    }

    /**
     * Fetches a Confluence page by page ID and returns its content as Markdown.
     *
     * @param pageId The Confluence page ID
     * @return PageMarkdownResult containing the markdown content and any attachment results
     * @throws Exception if the page cannot be fetched or transcribed
     */
    suspend fun getPageMarkdownByPageId(pageId: String): PageMarkdownResult {
        return pageMarkdownFetchFeature.fetch(pageId)
    }

    /**
     * Updates a Confluence page with markdown content.
     * Converts the markdown to ADF format and updates the page.
     *
     * @param url The full Confluence page URL
     * @param markdown The markdown content to update the page with
     * @param message Optional version message for the update
     * @return The updated page response
     * @throws IllegalArgumentException if the URL cannot be parsed to extract a page ID
     * @throws IllegalStateException if markdown transcription fails or page cannot be updated
     */
    suspend fun updatePageMarkdown(
        url: String,
        markdown: String,
        message: String? = null,
    ): PageResponse {
        val pageId =
            ConfluenceUrlParser.extractPageId(url)
                ?: throw IllegalArgumentException("Unable to extract page ID from URL: $url")

        return pageMarkdownUpdateFeature.update(pageId, markdown, message)
    }

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
    suspend fun updateTemplateMarkdown(
        templateId: String,
        markdown: String,
        name: String,
        templateType: String = "page",
    ): TemplateResponse {
        return templateMarkdownUpdateFeature.update(templateId, markdown, name, templateType)
    }

    /**
     * Closes the HTTP clients and releases resources.
     * Should be called when the Transcribe instance is no longer needed.
     */
    fun close() {
        httpClient.close()
        httpClientV1.close()
    }
}

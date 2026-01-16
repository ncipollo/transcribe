import api.atlassian.Attachment
import api.atlassian.AttachmentAPIClient
import api.atlassian.CommentAPIClient
import api.atlassian.ConfluenceHttpClientFactory
import api.atlassian.ConfluenceUrlParser
import api.atlassian.FooterComment
import api.atlassian.InlineComment
import api.atlassian.PageAPIClient
import api.atlassian.PageResponse
import api.atlassian.TemplateAPIClient
import api.atlassian.TemplateResponse
import context.ADFTranscriberContext
import context.AttachmentContext
import context.MarkdownContext
import context.PageContext
import fetch.PageFetchResult
import io.ktor.client.HttpClient
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import transcribe.atlassian.ConfluenceToMarkdownTranscriber
import transcribe.comment.CommentChildrenMapper
import transcribe.comment.CommentResult
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

        // Fetch page, attachments, and comments in parallel
        val (page, attachments, footerComments, inlineComments) = coroutineScope {
            val pageDeferred = async { pageApiClient.getPage(pageId) }
            val attachmentsDeferred = async { attachmentApiClient.getPageAttachments(pageId) }
            val footerCommentsDeferred = async { commentApiClient.getPageFooterComments(pageId) }
            val inlineCommentsDeferred = async { commentApiClient.getPageInlineComments(pageId) }

            PageFetchResult(
                page = pageDeferred.await(),
                attachments = attachmentsDeferred.await(),
                footerComments = footerCommentsDeferred.await(),
                inlineComments = inlineCommentsDeferred.await(),
            )
        }

        val adfBody =
            page.body?.atlasDocFormat?.docNode
                ?: throw IllegalStateException("Page $pageId does not contain ADF body content")

        // Apply toMarkdown transformer before transcribing
        val context = ADFTranscriberContext(
            pageContext = PageContext.fromPageResponse(page),
            attachmentContext = AttachmentContext.from(attachments),
        )
        val transformedContent = configuration.toMarkdownTransformer.transform(adfBody.content, context)
        val transformedDocNode = adfBody.copy(content = transformedContent)

        val result = transcriber.transcribe(transformedDocNode, context)

        // Handle actions from transcription result
        val actionResults = actionHandler.handleActions(result.actions)

        // Transform comments using CommentTransformer
        val commentResult = transformComments(footerComments, inlineComments, context)

        return PageMarkdownResult(
            markdown = result.content,
            attachmentResults = actionResults,
            commentResult = commentResult,
        )
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
        val transformedContent = configuration.toConfluenceTransformer.transform(docNode.content, context)
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
        val currentTemplate = templateApiClient.getTemplate(templateId)

        val context = MarkdownContext(
            markdownText = markdown,
            pageContext = PageContext.fromTemplateResponse(currentTemplate),
        )
        val result = markdownTranscriber.transcribe(markdown, context)
        val docNode = result.content

        // Apply toConfluence transformer after transcribing
        val transformedContent = configuration.toConfluenceTransformer.transform(docNode.content, context)
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

    /**
     * Closes the HTTP clients and releases resources.
     * Should be called when the Transcribe instance is no longer needed.
     */
    fun close() {
        httpClient.close()
        httpClientV1.close()
    }

    private suspend fun transformComments(
        footerComments: List<FooterComment>,
        inlineComments: List<InlineComment>,
        context: ADFTranscriberContext,
    ): CommentResult {
        // Fetch children for all comments in parallel
        val (footerChildrenMap, inlineChildrenMap) = coroutineScope {
            val footerDeferred = async { fetchFooterCommentChildren(footerComments) }
            val inlineDeferred = async { fetchInlineCommentChildren(inlineComments) }
            Pair(footerDeferred.await(), inlineDeferred.await())
        }

        // Build the children lookup map
        val childrenMap = CommentChildrenMapper.buildChildrenMap(
            footerChildrenMap,
            inlineChildrenMap,
            commentTransformer,
            context,
        )

        // Transform with children
        val transformedFooterComments = footerComments.map {
            commentTransformer.transformFooterComment(it, context, childrenMap)
        }
        val transformedInlineComments = inlineComments.map {
            commentTransformer.transformInlineComment(it, context, childrenMap)
        }

        return CommentResult(
            inlineComments = transformedInlineComments,
            footerComments = transformedFooterComments,
        )
    }

    private suspend fun fetchFooterCommentChildren(
        comments: List<FooterComment>,
    ): Map<String, List<FooterComment>> = coroutineScope {
        comments
            .map { comment ->
                async { comment.id to commentApiClient.getFooterCommentChildren(comment.id) }
            }
            .awaitAll()
            .toMap()
    }

    private suspend fun fetchInlineCommentChildren(
        comments: List<InlineComment>,
    ): Map<String, List<InlineComment>> = coroutineScope {
        comments
            .map { comment ->
                async { comment.id to commentApiClient.getInlineCommentChildren(comment.id) }
            }
            .awaitAll()
            .toMap()
    }
}

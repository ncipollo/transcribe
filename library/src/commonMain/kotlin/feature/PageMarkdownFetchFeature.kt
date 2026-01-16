package feature

import PageMarkdownResult
import PageMetadata
import api.atlassian.AttachmentAPIClient
import api.atlassian.CommentAPIClient
import api.atlassian.FooterComment
import api.atlassian.InlineComment
import api.atlassian.PageAPIClient
import context.ADFTranscriberContext
import context.AttachmentContext
import context.PageContext
import fetch.PageFetchResult
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import transcribe.action.ActionHandler
import transcribe.atlassian.ConfluenceToMarkdownTranscriber
import transcribe.comment.CommentChildrenMapper
import transcribe.comment.CommentResult
import transcribe.comment.CommentTransformer
import transcribe.comment.totalCommentCount
import transcribe.transformer.ADFTransformer

/**
 * Feature for fetching a Confluence page and converting it to Markdown.
 */
class PageMarkdownFetchFeature(
    private val pageApiClient: PageAPIClient,
    private val attachmentApiClient: AttachmentAPIClient,
    private val commentApiClient: CommentAPIClient,
    private val transcriber: ConfluenceToMarkdownTranscriber,
    private val commentTransformer: CommentTransformer,
    private val actionHandler: ActionHandler,
    private val toMarkdownTransformer: ADFTransformer<ADFTranscriberContext>,
) {
    /**
     * Fetches a Confluence page by ID and returns its content as Markdown.
     *
     * @param pageId The Confluence page ID
     * @return PageMarkdownResult containing the markdown content and any attachment results
     * @throws IllegalStateException if the page does not contain ADF body content
     */
    suspend fun fetch(pageId: String): PageMarkdownResult {
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
        val transformedContent = toMarkdownTransformer.transform(adfBody.content, context)
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
            metadata = PageMetadata(
                createdAt = page.createdAt,
                totalCommentCount = commentResult.totalCommentCount(),
            ),
        )
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

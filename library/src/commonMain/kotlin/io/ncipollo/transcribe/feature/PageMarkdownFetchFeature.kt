package io.ncipollo.transcribe.feature

import io.ncipollo.transcribe.PageMarkdownResult
import io.ncipollo.transcribe.PageMetadata
import io.ncipollo.transcribe.api.atlassian.AttachmentAPIClient
import io.ncipollo.transcribe.api.atlassian.CommentAPIClient
import io.ncipollo.transcribe.api.atlassian.FooterComment
import io.ncipollo.transcribe.api.atlassian.InlineComment
import io.ncipollo.transcribe.api.atlassian.PageAPIClient
import io.ncipollo.transcribe.context.ADFTranscriberContext
import io.ncipollo.transcribe.context.AttachmentContext
import io.ncipollo.transcribe.context.PageContext
import io.ncipollo.transcribe.fetch.PageFetchResult
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import io.ncipollo.transcribe.transcriber.action.ActionHandler
import io.ncipollo.transcribe.transcriber.atlassian.ConfluenceToMarkdownTranscriber
import io.ncipollo.transcribe.transcriber.comment.CommentChildrenMapper
import io.ncipollo.transcribe.transcriber.comment.CommentResult
import io.ncipollo.transcribe.transcriber.comment.CommentTransformer
import io.ncipollo.transcribe.transcriber.comment.totalCommentCount
import io.ncipollo.transcribe.transcriber.transformer.ADFTransformer

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
    private val siteName: String,
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

        val baseWikiUrl = "https://$siteName.atlassian.net/wiki"
        val pageUrl = page.links?.webui?.let { "$baseWikiUrl$it" }

        // Apply toMarkdown transformer before transcribing
        val context = ADFTranscriberContext(
            pageContext = PageContext.fromPageResponse(page),
            attachmentContext = AttachmentContext.from(attachments),
            baseWikiUrl = baseWikiUrl,
            pageUrl = pageUrl ?: "",
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
                suggestedDocumentName = context.suggestedDocumentName.takeUnless { it.isBlank() } ?: "document",
                pageUrl = pageUrl,
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

package io.ncipollo.transcribe

import io.ncipollo.transcribe.transcriber.action.AttachmentResult
import io.ncipollo.transcribe.transcriber.comment.CommentResult

data class PageMetadata(
    val createdAt: String? = null,
    val totalCommentCount: Int = 0,
    val suggestedDocumentName: String = "",
    val pageUrl: String? = null,
)

data class PageMarkdownResult(
    val markdown: String = "",
    val attachmentResults: List<AttachmentResult> = emptyList(),
    val commentResult: CommentResult = CommentResult(),
    val metadata: PageMetadata = PageMetadata(),
)

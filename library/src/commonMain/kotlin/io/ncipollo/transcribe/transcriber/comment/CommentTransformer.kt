package io.ncipollo.transcribe.transcriber.comment

import io.ncipollo.transcribe.api.atlassian.FooterComment
import io.ncipollo.transcribe.api.atlassian.InlineComment
import io.ncipollo.transcribe.context.ADFTranscriberContext
import io.ncipollo.transcribe.data.atlassian.adf.DocNode
import kotlinx.serialization.json.Json
import io.ncipollo.transcribe.transcriber.atlassian.ConfluenceToMarkdownTranscriber

class CommentTransformer(
    private val transcriber: ConfluenceToMarkdownTranscriber,
) {
    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }

    fun transformFooterComment(
        footerComment: FooterComment,
        context: ADFTranscriberContext,
        childrenMap: Map<String, List<Comment>> = emptyMap(),
    ): Comment {
        val docNode = json.decodeFromString<DocNode>(footerComment.body.atlasDocFormat.value)
        val transcribeResult = transcriber.transcribe(docNode, context)

        return Comment(
            id = footerComment.id,
            authorId = footerComment.version.authorId,
            createdAt = footerComment.version.createdAt,
            content = transcribeResult.content,
            commentType = CommentType.FOOTER,
            parentCommentId = footerComment.parentCommentId,
            children = childrenMap[footerComment.id] ?: emptyList(),
        )
    }

    fun transformInlineComment(
        inlineComment: InlineComment,
        context: ADFTranscriberContext,
        childrenMap: Map<String, List<Comment>> = emptyMap(),
    ): Comment {
        val docNode = json.decodeFromString<DocNode>(inlineComment.body.atlasDocFormat.value)
        val transcribeResult = transcriber.transcribe(docNode, context)

        return Comment(
            id = inlineComment.id,
            authorId = inlineComment.version.authorId,
            createdAt = inlineComment.version.createdAt,
            content = transcribeResult.content,
            commentType = CommentType.INLINE,
            parentCommentId = null,
            children = childrenMap[inlineComment.id] ?: emptyList(),
        )
    }
}

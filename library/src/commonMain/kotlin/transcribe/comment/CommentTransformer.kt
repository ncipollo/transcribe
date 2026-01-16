package transcribe.comment

import api.atlassian.FooterComment
import api.atlassian.InlineComment
import context.ADFTranscriberContext
import data.atlassian.adf.DocNode
import kotlinx.serialization.json.Json
import transcribe.atlassian.ConfluenceToMarkdownTranscriber

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
        )
    }

    fun transformInlineComment(
        inlineComment: InlineComment,
        context: ADFTranscriberContext,
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
        )
    }
}

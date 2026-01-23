package io.ncipollo.transcribe.transcriber.comment

import io.ncipollo.transcribe.api.atlassian.FooterComment
import io.ncipollo.transcribe.api.atlassian.InlineComment
import io.ncipollo.transcribe.context.ADFTranscriberContext

object CommentChildrenMapper {
    fun buildChildrenMap(
        footerChildrenByParent: Map<String, List<FooterComment>>,
        inlineChildrenByParent: Map<String, List<InlineComment>>,
        transformer: CommentTransformer,
        context: ADFTranscriberContext,
    ): Map<String, List<Comment>> {
        val childrenMap = mutableMapOf<String, List<Comment>>()

        footerChildrenByParent.forEach { (parentId, children) ->
            childrenMap[parentId] = children.map {
                transformer.transformFooterComment(it, context)
            }
        }

        inlineChildrenByParent.forEach { (parentId, children) ->
            childrenMap[parentId] = children.map {
                transformer.transformInlineComment(it, context)
            }
        }

        return childrenMap
    }
}

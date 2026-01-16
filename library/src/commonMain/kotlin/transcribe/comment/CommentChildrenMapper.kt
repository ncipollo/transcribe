package transcribe.comment

import api.atlassian.FooterComment
import api.atlassian.InlineComment
import context.ADFTranscriberContext

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

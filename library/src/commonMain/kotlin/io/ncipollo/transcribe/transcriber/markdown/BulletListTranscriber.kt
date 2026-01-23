package io.ncipollo.transcribe.transcriber.markdown

import io.ncipollo.transcribe.context.MarkdownContext
import io.ncipollo.transcribe.data.atlassian.adf.ADFBlockNode
import io.ncipollo.transcribe.data.atlassian.adf.BulletListNode
import org.intellij.markdown.ast.ASTNode
import io.ncipollo.transcribe.transcriber.TranscribeResult

/**
 * Transcriber for UNORDERED_LIST nodes that converts markdown bullet lists to ADF BulletListNode.
 * If all list items contain checkboxes, it will transcribe as a TaskListNode instead.
 */
class BulletListTranscriber(
    private val nodeMapper: MarkdownNodeMapper,
) : MarkdownTranscriber<ADFBlockNode> {
    override fun transcribe(
        input: ASTNode,
        context: MarkdownContext,
    ): TranscribeResult<ADFBlockNode> {
        return ListTranscriberHelper.transcribe(input, context, nodeMapper) { listItems ->
            BulletListNode(content = listItems)
        }
    }
}

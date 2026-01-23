package io.ncipollo.transcribe.transcriber.markdown

import io.ncipollo.transcribe.context.MarkdownContext
import io.ncipollo.transcribe.data.atlassian.adf.ADFBlockNode
import io.ncipollo.transcribe.data.atlassian.adf.ListItemNode
import org.intellij.markdown.ast.ASTNode
import io.ncipollo.transcribe.transcriber.TranscribeResult
import io.ncipollo.transcribe.transcriber.action.TranscriberAction

/**
 * Transcriber for LIST_ITEM nodes that converts markdown list items to ADF ListItemNode.
 */
class ListItemTranscriber(
    private val nodeMapper: MarkdownNodeMapper,
) : MarkdownTranscriber<ListItemNode> {
    override fun transcribe(
        input: ASTNode,
        context: MarkdownContext,
    ): TranscribeResult<ListItemNode> {
        val contentNodes = mutableListOf<ADFBlockNode>()
        val allActions = mutableListOf<TranscriberAction>()

        // Process all children that have a registered transcriber
        input.children.forEach { child ->
            nodeMapper.transcriberFor(child.type)?.let { transcriber ->
                val result = transcriber.transcribe(child, context)
                // Only include block nodes in list item content
                if (result.content is ADFBlockNode) {
                    contentNodes.add(result.content)
                }
                allActions.addAll(result.actions)
            }
        }

        return TranscribeResult(
            ListItemNode(content = contentNodes),
            allActions
        )
    }
}

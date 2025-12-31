package transcribe.markdown

import data.atlassian.adf.ADFBlockNode
import data.atlassian.adf.OrderedListNode
import org.intellij.markdown.ast.ASTNode
import transcribe.TranscribeResult

/**
 * Transcriber for ORDERED_LIST nodes that converts markdown ordered lists to ADF OrderedListNode.
 * If all list items contain checkboxes, it will transcribe as a TaskListNode instead.
 */
class OrderedListTranscriber(
    private val nodeMapper: MarkdownNodeMapper,
) : MarkdownTranscriber<ADFBlockNode> {
    override fun transcribe(
        input: ASTNode,
        context: MarkdownContext,
    ): TranscribeResult<ADFBlockNode> {
        return ListTranscriberHelper.transcribe(input, context, nodeMapper) { listItems ->
            OrderedListNode(content = listItems)
        }
    }
}

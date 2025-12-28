package transcribe.markdown

import data.atlassian.adf.OrderedListNode
import org.intellij.markdown.MarkdownElementTypes
import org.intellij.markdown.ast.ASTNode
import transcribe.TranscribeResult

/**
 * Transcriber for ORDERED_LIST nodes that converts markdown ordered lists to ADF OrderedListNode.
 */
class OrderedListTranscriber(
    private val nodeMapper: MarkdownNodeMapper,
) : MarkdownTranscriber<OrderedListNode> {
    override fun transcribe(
        input: ASTNode,
        context: MarkdownContext,
    ): TranscribeResult<OrderedListNode> {
        // Extract all LIST_ITEM children
        val listItems =
            input.children
                .filter { it.type == MarkdownElementTypes.LIST_ITEM }
                .map { itemNode ->
                    val listItemTranscriber = ListItemTranscriber(nodeMapper)
                    listItemTranscriber.transcribe(itemNode, context).content
                }

        return TranscribeResult(
            OrderedListNode(
                content = listItems,
            ),
        )
    }
}

package transcribe.markdown

import data.atlassian.adf.BulletListNode
import org.intellij.markdown.MarkdownElementTypes
import org.intellij.markdown.ast.ASTNode
import transcribe.TranscribeResult

/**
 * Transcriber for UNORDERED_LIST nodes that converts markdown bullet lists to ADF BulletListNode.
 */
class BulletListTranscriber(
    private val nodeMapper: MarkdownNodeMapper,
) : MarkdownTranscriber<BulletListNode> {
    override fun transcribe(
        input: ASTNode,
        context: MarkdownContext,
    ): TranscribeResult<BulletListNode> {
        // Extract all LIST_ITEM children
        val listItems = input.children
            .filter { it.type == MarkdownElementTypes.LIST_ITEM }
            .mapNotNull { itemNode ->
                val listItemTranscriber = ListItemTranscriber(nodeMapper)
                listItemTranscriber.transcribe(itemNode, context).content
            }

        return TranscribeResult(
            BulletListNode(
                content = listItems,
            ),
        )
    }
}


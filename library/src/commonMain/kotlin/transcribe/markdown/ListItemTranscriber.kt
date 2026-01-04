package transcribe.markdown

import context.MarkdownContext
import data.atlassian.adf.ListItemNode
import org.intellij.markdown.MarkdownElementTypes
import org.intellij.markdown.ast.ASTNode
import org.intellij.markdown.ast.findChildOfType
import transcribe.TranscribeResult

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
        val result =
            findParagraphNode(input)?.let {
                val transcriber = nodeMapper.transcriberFor(it.type)
                transcriber?.transcribe(it, context)
            }
        val paragraphContent = result?.content
        val actions = result?.actions ?: emptyList()
        return TranscribeResult(ListItemNode(content = listOfNotNull(paragraphContent)), actions)
    }

    private fun findParagraphNode(input: ASTNode): ASTNode? = input.findChildOfType(MarkdownElementTypes.PARAGRAPH)
}

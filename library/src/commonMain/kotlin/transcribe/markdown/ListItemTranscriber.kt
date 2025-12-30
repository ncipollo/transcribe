package transcribe.markdown

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
        val paragraphContent =
            findParagraphNode(input)?.let {
                val transcriber = nodeMapper.transcriberFor(it.type)
                transcriber?.transcribe(it, context)?.content
            }
        return TranscribeResult(ListItemNode(content = listOfNotNull(paragraphContent)))
    }

    private fun findParagraphNode(input: ASTNode): ASTNode? = input.findChildOfType(MarkdownElementTypes.PARAGRAPH)
}

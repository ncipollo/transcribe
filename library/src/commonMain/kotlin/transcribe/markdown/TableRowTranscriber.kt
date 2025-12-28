package transcribe.markdown

import data.atlassian.adf.TableRowNode
import org.intellij.markdown.ast.ASTNode
import org.intellij.markdown.flavours.gfm.GFMTokenTypes
import transcribe.TranscribeResult

/**
 * Transcriber for ROW and HEADER nodes (GFM) that converts markdown table rows to ADF TableRowNode.
 */
class TableRowTranscriber(
    private val nodeMapper: MarkdownNodeMapper,
    private val isHeader: Boolean = false,
) : MarkdownTranscriber<TableRowNode> {
    override fun transcribe(
        input: ASTNode,
        context: MarkdownContext,
    ): TranscribeResult<TableRowNode> {
        // Extract CELL children
        val cells =
            input.children
                .filter { it.type == GFMTokenTypes.CELL }
                .map { cellNode ->
                    val tableCellTranscriber = TableCellTranscriber(nodeMapper, isHeader)
                    tableCellTranscriber.transcribe(cellNode, context).content
                }

        return TranscribeResult(
            TableRowNode(
                content = cells,
            ),
        )
    }
}

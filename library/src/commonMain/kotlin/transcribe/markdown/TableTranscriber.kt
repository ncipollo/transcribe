package transcribe.markdown

import data.atlassian.adf.TableNode
import org.intellij.markdown.ast.ASTNode
import org.intellij.markdown.flavours.gfm.GFMElementTypes
import transcribe.TranscribeResult

/**
 * Transcriber for TABLE nodes (GFM) that converts markdown tables to ADF TableNode.
 */
class TableTranscriber(
    private val nodeMapper: MarkdownNodeMapper,
) : MarkdownTranscriber<TableNode> {
    override fun transcribe(
        input: ASTNode,
        context: MarkdownContext,
    ): TranscribeResult<TableNode> {
        // Extract HEADER and ROW children
        val rows =
            input.children
                .filter { it.type == GFMElementTypes.HEADER || it.type == GFMElementTypes.ROW }
                .map { rowNode ->
                    val isHeader = rowNode.type == GFMElementTypes.HEADER
                    val tableRowTranscriber = TableRowTranscriber(nodeMapper, isHeader)
                    tableRowTranscriber.transcribe(rowNode, context).content
                }

        return TranscribeResult(
            TableNode(
                content = rows,
            ),
        )
    }
}

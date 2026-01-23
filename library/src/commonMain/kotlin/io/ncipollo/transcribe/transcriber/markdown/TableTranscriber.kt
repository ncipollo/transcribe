package io.ncipollo.transcribe.transcriber.markdown

import io.ncipollo.transcribe.context.MarkdownContext
import io.ncipollo.transcribe.data.atlassian.adf.TableNode
import org.intellij.markdown.ast.ASTNode
import org.intellij.markdown.flavours.gfm.GFMElementTypes
import io.ncipollo.transcribe.transcriber.TranscribeResult

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
        val results =
            input.children
                .filter { it.type == GFMElementTypes.HEADER || it.type == GFMElementTypes.ROW }
                .map { rowNode ->
                    val isHeader = rowNode.type == GFMElementTypes.HEADER
                    val tableRowTranscriber = TableRowTranscriber(nodeMapper, isHeader)
                    tableRowTranscriber.transcribe(rowNode, context)
                }
        val rows = results.map { it.content }
        val actions = results.flatMap { it.actions }

        return TranscribeResult(
            TableNode(
                content = rows,
            ),
            actions,
        )
    }
}

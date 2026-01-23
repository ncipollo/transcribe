package io.ncipollo.transcribe.transcriber.atlassian

import io.ncipollo.transcribe.context.ADFTranscriberContext
import io.ncipollo.transcribe.data.atlassian.adf.TableNode
import io.ncipollo.transcribe.data.atlassian.adf.TableRowNode
import io.ncipollo.transcribe.transcriber.TranscribeResult

/**
 * Transcriber for TableNode that converts ADF table to GitHub markdown table.
 * Outputs table rows with separator row after header.
 */
class TableNodeTranscriber(
    private val mapper: ADFNodeMapper,
) : ADFTranscriber<TableNode> {
    override fun transcribe(
        input: TableNode,
        context: ADFTranscriberContext,
    ): TranscribeResult<String> {
        val rows = input.content
        if (rows.isEmpty()) {
            return TranscribeResult("")
        }

        val nodeTranscriber = ADFNodeTranscriber(mapper)
        val results = rows.map { row -> nodeTranscriber.transcribe(row, context) }
        val markdownRows = results.map { it.content }
        val actions = results.flatMap { it.actions }

        // Check if first row contains headers
        val firstRow = rows.firstOrNull()
        val hasHeaders = firstRow is TableRowNode && firstRow.content.any { it is io.ncipollo.transcribe.data.atlassian.adf.TableHeaderNode }

        val table =
            if (hasHeaders && markdownRows.isNotEmpty()) {
                // Add separator row after header
                val columnCount = markdownRows.first().split("|").size - 2 // Subtract empty strings at start/end
                val separator = "| " + List(columnCount) { "---" }.joinToString(" | ") + " |"
                markdownRows.take(1) + separator + markdownRows.drop(1)
            } else {
                markdownRows
            }

        return TranscribeResult(table.joinToString("\n"), actions)
    }
}

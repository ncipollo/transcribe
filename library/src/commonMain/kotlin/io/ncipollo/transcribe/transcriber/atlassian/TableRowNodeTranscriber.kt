package io.ncipollo.transcribe.transcriber.atlassian

import io.ncipollo.transcribe.context.ADFTranscriberContext
import io.ncipollo.transcribe.data.atlassian.adf.TableCellNode
import io.ncipollo.transcribe.data.atlassian.adf.TableHeaderNode
import io.ncipollo.transcribe.data.atlassian.adf.TableRowNode
import io.ncipollo.transcribe.transcriber.TranscribeResult

/**
 * Transcriber for TableRowNode that converts ADF table row to markdown.
 * Outputs | cell | cell | format.
 */
class TableRowNodeTranscriber(
    private val mapper: ADFNodeMapper,
) : ADFTranscriber<TableRowNode> {
    override fun transcribe(
        input: TableRowNode,
        context: ADFTranscriberContext,
    ): TranscribeResult<String> {
        val content = input.content
        if (content.isEmpty()) {
            return TranscribeResult("")
        }

        val nodeTranscriber = ADFNodeTranscriber(mapper)
        val results = content.map { cell ->
            when (cell) {
                is TableCellNode -> nodeTranscriber.transcribe(cell, context)
                is TableHeaderNode -> nodeTranscriber.transcribe(cell, context)
                else -> TranscribeResult("")
            }
        }
        val cells = results.map { it.content }
        val actions = results.flatMap { it.actions }

        val row = "| " + cells.joinToString(" | ") + " |"
        return TranscribeResult(row, actions)
    }
}

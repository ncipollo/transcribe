package io.ncipollo.transcribe.transcriber.atlassian

import io.ncipollo.transcribe.context.ADFTranscriberContext
import io.ncipollo.transcribe.data.atlassian.adf.TableCellNode
import io.ncipollo.transcribe.transcriber.TranscribeResult

/**
 * Transcriber for TableCellNode that converts ADF table cell to markdown.
 * Transcribes block content within the cell.
 */
class TableCellNodeTranscriber(
    private val mapper: ADFNodeMapper,
) : ADFTranscriber<TableCellNode> {
    override fun transcribe(
        input: TableCellNode,
        context: ADFTranscriberContext,
    ): TranscribeResult<String> {
        val content = input.content
        if (content.isEmpty()) {
            return TranscribeResult("")
        }

        val nodeTranscriber = ADFNodeTranscriber(mapper)
        val results = content.map { block -> nodeTranscriber.transcribe(block, context) }
        val markdown = results.joinToString("") { it.content }
        val actions = results.flatMap { it.actions }
        return TranscribeResult(markdown.trim(), actions)
    }
}

package transcribe.atlassian

import context.ADFTranscriberContext
import data.atlassian.adf.TableCellNode
import transcribe.TranscribeResult

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

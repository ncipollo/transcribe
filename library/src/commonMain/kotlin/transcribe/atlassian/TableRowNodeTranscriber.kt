package transcribe.atlassian

import data.atlassian.adf.TableCellNode
import data.atlassian.adf.TableHeaderNode
import data.atlassian.adf.TableRowNode
import transcribe.TranscribeResult

/**
 * Transcriber for TableRowNode that converts ADF table row to markdown.
 * Outputs | cell | cell | format.
 */
class TableRowNodeTranscriber(
    private val mapper: ADFNodeMapper,
) : ADFTranscriber<TableRowNode> {
    override fun transcribe(input: TableRowNode, context: ADFTranscriberContext): TranscribeResult<String> {
        val content = input.content
        if (content.isEmpty()) {
            return TranscribeResult("")
        }

        val nodeTranscriber = ADFNodeTranscriber(mapper)
        val cells =
            content.map { cell ->
                when (cell) {
                    is TableCellNode -> nodeTranscriber.transcribe(cell, context).content
                    is TableHeaderNode -> nodeTranscriber.transcribe(cell, context).content
                    else -> ""
                }
            }

        val row = "| " + cells.joinToString(" | ") + " |"
        return TranscribeResult(row)
    }
}

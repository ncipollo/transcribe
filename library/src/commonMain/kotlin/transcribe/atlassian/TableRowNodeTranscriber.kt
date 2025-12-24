package transcribe.atlassian

import data.atlassian.adf.TableCellNode
import data.atlassian.adf.TableHeaderNode
import data.atlassian.adf.TableRowNode
import transcribe.TranscribeResult

/**
 * Transcriber for TableRowNode that converts ADF table row to markdown.
 * Outputs | cell | cell | format.
 */
class TableRowNodeTranscriber : ADFTranscriber<TableRowNode> {
    override fun transcribe(input: TableRowNode): TranscribeResult<String> {
        val content = input.content
        if (content.isEmpty()) {
            return TranscribeResult("")
        }
        
        val cells = content.map { cell ->
            when (cell) {
                is TableCellNode -> ADFNodeTranscriber.transcribeBlock(cell).content
                is TableHeaderNode -> ADFNodeTranscriber.transcribeBlock(cell).content
                else -> ""
            }
        }
        
        val row = "| " + cells.joinToString(" | ") + " |"
        return TranscribeResult(row)
    }
}


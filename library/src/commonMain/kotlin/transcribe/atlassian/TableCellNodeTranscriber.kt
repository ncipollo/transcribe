package transcribe.atlassian

import data.atlassian.adf.TableCellNode
import transcribe.TranscribeResult

/**
 * Transcriber for TableCellNode that converts ADF table cell to markdown.
 * Transcribes block content within the cell.
 */
class TableCellNodeTranscriber : ADFTranscriber<TableCellNode> {
    override fun transcribe(input: TableCellNode): TranscribeResult<String> {
        val content = input.content
        if (content.isEmpty()) {
            return TranscribeResult("")
        }
        
        val markdown = content.joinToString("") { block ->
            ADFNodeTranscriber.transcribeBlock(block).content
        }
        
        return TranscribeResult(markdown.trim())
    }
}


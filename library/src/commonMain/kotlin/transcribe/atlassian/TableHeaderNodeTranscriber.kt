package transcribe.atlassian

import data.atlassian.adf.TableHeaderNode
import transcribe.TranscribeResult

/**
 * Transcriber for TableHeaderNode that converts ADF table header cell to markdown.
 * Transcribes block content within the header cell.
 */
class TableHeaderNodeTranscriber : ADFTranscriber<TableHeaderNode> {
    override fun transcribe(input: TableHeaderNode): TranscribeResult<String> {
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


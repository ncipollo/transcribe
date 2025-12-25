package transcribe.atlassian

import data.atlassian.adf.TableCellNode
import transcribe.TranscribeResult

/**
 * Transcriber for TableCellNode that converts ADF table cell to markdown.
 * Transcribes block content within the cell.
 */
class TableCellNodeTranscriber(
    private val mapper: ADFNodeMapper,
) : ADFTranscriber<TableCellNode> {
    override fun transcribe(input: TableCellNode): TranscribeResult<String> {
        val content = input.content
        if (content.isEmpty()) {
            return TranscribeResult("")
        }

        val nodeTranscriber = ADFNodeTranscriber(mapper)
        val markdown =
            content.joinToString("") { block ->
                nodeTranscriber.transcribe(block).content
            }

        return TranscribeResult(markdown.trim())
    }
}

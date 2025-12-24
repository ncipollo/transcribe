package transcribe.atlassian

import data.atlassian.adf.ADFNode
import data.atlassian.adf.TableCellNode
import kotlin.reflect.KClass
import transcribe.TranscribeResult

/**
 * Transcriber for TableCellNode that converts ADF table cell to markdown.
 * Transcribes block content within the cell.
 */
class TableCellNodeTranscriber(
    private val nodeMap: Map<KClass<out ADFNode>, ADFTranscriber<*>>
) : ADFTranscriber<TableCellNode> {
    override fun transcribe(input: TableCellNode): TranscribeResult<String> {
        val content = input.content
        if (content.isEmpty()) {
            return TranscribeResult("")
        }
        
        val nodeTranscriber = ADFNodeTranscriber(nodeMap)
        val markdown = content.joinToString("") { block ->
            nodeTranscriber.transcribeBlock(block).content
        }
        
        return TranscribeResult(markdown.trim())
    }
}


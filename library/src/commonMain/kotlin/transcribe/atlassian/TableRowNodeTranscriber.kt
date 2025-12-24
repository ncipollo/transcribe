package transcribe.atlassian

import data.atlassian.adf.ADFNode
import data.atlassian.adf.TableCellNode
import data.atlassian.adf.TableHeaderNode
import data.atlassian.adf.TableRowNode
import kotlin.reflect.KClass
import transcribe.TranscribeResult

/**
 * Transcriber for TableRowNode that converts ADF table row to markdown.
 * Outputs | cell | cell | format.
 */
class TableRowNodeTranscriber(
    private val nodeMap: Map<KClass<out ADFNode>, ADFTranscriber<*>>
) : ADFTranscriber<TableRowNode> {
    override fun transcribe(input: TableRowNode): TranscribeResult<String> {
        val content = input.content
        if (content.isEmpty()) {
            return TranscribeResult("")
        }
        
        val nodeTranscriber = ADFNodeTranscriber(nodeMap)
        val cells = content.map { cell ->
            when (cell) {
                is TableCellNode -> nodeTranscriber.transcribeBlock(cell).content
                is TableHeaderNode -> nodeTranscriber.transcribeBlock(cell).content
                else -> ""
            }
        }
        
        val row = "| " + cells.joinToString(" | ") + " |"
        return TranscribeResult(row)
    }
}


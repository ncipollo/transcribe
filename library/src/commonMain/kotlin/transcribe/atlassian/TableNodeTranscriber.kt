package transcribe.atlassian

import data.atlassian.adf.ADFNode
import data.atlassian.adf.TableNode
import data.atlassian.adf.TableRowNode
import kotlin.reflect.KClass
import transcribe.TranscribeResult

/**
 * Transcriber for TableNode that converts ADF table to GitHub markdown table.
 * Outputs table rows with separator row after header.
 */
class TableNodeTranscriber(
    private val nodeMap: Map<KClass<out ADFNode>, ADFTranscriber<*>>
) : ADFTranscriber<TableNode> {
    override fun transcribe(input: TableNode): TranscribeResult<String> {
        val rows = input.content
        if (rows.isEmpty()) {
            return TranscribeResult("")
        }
        
        val nodeTranscriber = ADFNodeTranscriber(nodeMap)
        val markdownRows = rows.map { row ->
            nodeTranscriber.transcribe(row).content
        }
        
        // Check if first row contains headers
        val firstRow = rows.firstOrNull()
        val hasHeaders = firstRow is TableRowNode && firstRow.content.any { it is data.atlassian.adf.TableHeaderNode }
        
        val table = if (hasHeaders && markdownRows.isNotEmpty()) {
            // Add separator row after header
            val columnCount = markdownRows.first().split("|").size - 2 // Subtract empty strings at start/end
            val separator = "| " + List(columnCount) { "---" }.joinToString(" | ") + " |"
            markdownRows.take(1) + separator + markdownRows.drop(1)
        } else {
            markdownRows
        }
        
        return TranscribeResult(table.joinToString("\n") + "\n\n")
    }
}


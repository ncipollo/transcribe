package transcribe.atlassian

import data.atlassian.adf.ADFNode
import data.atlassian.adf.TableHeaderNode
import kotlin.reflect.KClass
import transcribe.TranscribeResult

/**
 * Transcriber for TableHeaderNode that converts ADF table header cell to markdown.
 * Transcribes block content within the header cell.
 */
class TableHeaderNodeTranscriber(
    private val nodeMap: Map<KClass<out ADFNode>, ADFTranscriber<*>>
) : ADFTranscriber<TableHeaderNode> {
    override fun transcribe(input: TableHeaderNode): TranscribeResult<String> {
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


package transcribe.atlassian

import context.ADFTranscriberContext
import data.atlassian.adf.TableHeaderNode
import transcribe.TranscribeResult

/**
 * Transcriber for TableHeaderNode that converts ADF table header cell to markdown.
 * Transcribes block content within the header cell.
 */
class TableHeaderNodeTranscriber(
    private val mapper: ADFNodeMapper,
) : ADFTranscriber<TableHeaderNode> {
    override fun transcribe(
        input: TableHeaderNode,
        context: ADFTranscriberContext,
    ): TranscribeResult<String> {
        val content = input.content
        if (content.isEmpty()) {
            return TranscribeResult("")
        }

        val nodeTranscriber = ADFNodeTranscriber(mapper)
        val markdown =
            content.joinToString("") { block ->
                nodeTranscriber.transcribe(block, context).content
            }

        return TranscribeResult(markdown.trim())
    }
}

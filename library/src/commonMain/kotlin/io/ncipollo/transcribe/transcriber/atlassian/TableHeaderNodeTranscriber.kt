package io.ncipollo.transcribe.transcriber.atlassian

import io.ncipollo.transcribe.context.ADFTranscriberContext
import io.ncipollo.transcribe.data.atlassian.adf.TableHeaderNode
import io.ncipollo.transcribe.transcriber.TranscribeResult

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
        val results = content.map { block -> nodeTranscriber.transcribe(block, context) }
        val markdown = results.joinToString("") { it.content }
        val actions = results.flatMap { it.actions }
        return TranscribeResult(markdown.trim(), actions)
    }
}

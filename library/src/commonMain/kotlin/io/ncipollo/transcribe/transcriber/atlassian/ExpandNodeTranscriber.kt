package io.ncipollo.transcribe.transcriber.atlassian

import io.ncipollo.transcribe.context.ADFTranscriberContext
import io.ncipollo.transcribe.data.atlassian.adf.ExpandNode
import io.ncipollo.transcribe.transcriber.TranscribeResult

/**
 * Transcriber for ExpandNode that converts ADF expand to markdown HTML details element.
 * Outputs <details> and <summary> tags with the title from attrs or default "Click to expand".
 */
class ExpandNodeTranscriber(
    private val mapper: ADFNodeMapper,
) : ADFTranscriber<ExpandNode> {
    override fun transcribe(
        input: ExpandNode,
        context: ADFTranscriberContext,
    ): TranscribeResult<String> {
        val content = input.content
        val summaryText = input.attrs?.title ?: "Click to expand"

        if (content.isEmpty()) {
            return TranscribeResult("<details>\n<summary>$summaryText</summary>\n\n</details>\n")
        }

        val nodeTranscriber = ADFNodeTranscriber(mapper)
        val results = content.map { block -> nodeTranscriber.transcribe(block, context) }
        val markdown = results.joinToString("") { it.content }
        val actions = results.flatMap { it.actions }
        return TranscribeResult("<details>\n<summary>$summaryText</summary>\n\n$markdown</details>\n", actions)
    }
}

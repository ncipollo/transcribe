package io.ncipollo.transcribe.transcriber.atlassian

import io.ncipollo.transcribe.context.ADFTranscriberContext
import io.ncipollo.transcribe.data.atlassian.adf.ParagraphNode
import io.ncipollo.transcribe.transcriber.TranscribeResult

/**
 * Transcriber for ParagraphNode that converts ADF paragraph to markdown.
 * Transcribes inline content and appends newline.
 */
class ParagraphNodeTranscriber(
    private val mapper: ADFNodeMapper,
) : ADFTranscriber<ParagraphNode> {
    override fun transcribe(
        input: ParagraphNode,
        context: ADFTranscriberContext,
    ): TranscribeResult<String> {
        val content = input.content
        val markdown: String
        val actions: List<io.ncipollo.transcribe.transcriber.action.TranscriberAction>
        if (content.isNullOrEmpty()) {
            markdown = ""
            actions = emptyList()
        } else {
            val nodeTranscriber = ADFNodeTranscriber(mapper)
            val results = content.map { node -> nodeTranscriber.transcribe(node, context) }
            markdown = results.joinToString("") { it.content }
            actions = results.flatMap { it.actions }
        }
        return TranscribeResult("$markdown\n", actions)
    }
}

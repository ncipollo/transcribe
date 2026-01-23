package io.ncipollo.transcribe.transcriber.atlassian

import io.ncipollo.transcribe.context.ADFTranscriberContext
import io.ncipollo.transcribe.data.atlassian.adf.DocNode
import io.ncipollo.transcribe.transcriber.TranscribeResult

class ADFDocumentTranscriber(
    private val mapper: ADFNodeMapper,
) : ADFTranscriber<DocNode> {
    override fun transcribe(
        input: DocNode,
        context: ADFTranscriberContext,
    ): TranscribeResult<String> {
        val nodeTranscriber = ADFNodeTranscriber(mapper)
        val results = input.content.map { node -> nodeTranscriber.transcribe(node, context) }
        val markdown = results.joinToString("") { it.content }
        val actions = results.flatMap { it.actions }
        return TranscribeResult(markdown, actions)
    }
}

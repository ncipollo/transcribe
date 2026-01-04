package transcribe.atlassian

import context.ADFTranscriberContext
import data.atlassian.adf.DocNode
import transcribe.TranscribeResult

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

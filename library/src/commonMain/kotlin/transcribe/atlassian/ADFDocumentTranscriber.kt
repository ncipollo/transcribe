package transcribe.atlassian

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
        val markdown =
            input.content.joinToString("") { node ->
                nodeTranscriber.transcribe(node, context).content
            }
        return TranscribeResult(markdown)
    }
}

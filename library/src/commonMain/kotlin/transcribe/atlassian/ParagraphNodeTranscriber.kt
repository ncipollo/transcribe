package transcribe.atlassian

import context.ADFTranscriberContext
import data.atlassian.adf.ParagraphNode
import transcribe.TranscribeResult

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
        val actions: List<transcribe.action.TranscriberAction>
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

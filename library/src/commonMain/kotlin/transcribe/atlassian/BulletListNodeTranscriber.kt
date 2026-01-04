package transcribe.atlassian

import context.ADFTranscriberContext
import data.atlassian.adf.BulletListNode
import transcribe.TranscribeResult

/**
 * Transcriber for BulletListNode that converts ADF bullet list to markdown.
 * Outputs - prefixed items, one per line.
 */
class BulletListNodeTranscriber(
    private val mapper: ADFNodeMapper,
) : ADFTranscriber<BulletListNode> {
    override fun transcribe(
        input: BulletListNode,
        context: ADFTranscriberContext,
    ): TranscribeResult<String> {
        val content = input.content
        if (content.isEmpty()) {
            return TranscribeResult("")
        }

        val nodeTranscriber = ADFNodeTranscriber(mapper)
        val results = content.map { item -> nodeTranscriber.transcribe(item, context) }
        val markdown = results.joinToString("") { "- ${it.content}" }
        val actions = results.flatMap { it.actions }
        return TranscribeResult(markdown, actions)
    }
}

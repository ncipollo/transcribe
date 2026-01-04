package transcribe.atlassian

import context.ADFTranscriberContext
import data.atlassian.adf.OrderedListNode
import transcribe.TranscribeResult

/**
 * Transcriber for OrderedListNode that converts ADF ordered list to markdown.
 * Outputs numbered items (1., 2., etc.), starting from order attribute if present.
 */
class OrderedListNodeTranscriber(
    private val mapper: ADFNodeMapper,
) : ADFTranscriber<OrderedListNode> {
    override fun transcribe(
        input: OrderedListNode,
        context: ADFTranscriberContext,
    ): TranscribeResult<String> {
        val content = input.content
        if (content.isEmpty()) {
            return TranscribeResult("")
        }

        val nodeTranscriber = ADFNodeTranscriber(mapper)
        val startOrder = input.attrs?.order ?: 1
        val results = content.map { item -> nodeTranscriber.transcribe(item, context) }
        val markdown = results.mapIndexed { index, result ->
            val itemNumber = startOrder + index
            "$itemNumber. ${result.content}"
        }.joinToString("")
        val actions = results.flatMap { it.actions }
        return TranscribeResult(markdown, actions)
    }
}

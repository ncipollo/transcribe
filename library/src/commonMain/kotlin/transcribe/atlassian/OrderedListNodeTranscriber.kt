package transcribe.atlassian

import data.atlassian.adf.ADFNode
import data.atlassian.adf.OrderedListNode
import kotlin.reflect.KClass
import transcribe.TranscribeResult

/**
 * Transcriber for OrderedListNode that converts ADF ordered list to markdown.
 * Outputs numbered items (1., 2., etc.), starting from order attribute if present.
 */
class OrderedListNodeTranscriber(
    private val nodeMap: Map<KClass<out ADFNode>, ADFTranscriber<*>>
) : ADFTranscriber<OrderedListNode> {
    override fun transcribe(input: OrderedListNode): TranscribeResult<String> {
        val content = input.content
        if (content.isEmpty()) {
            return TranscribeResult("")
        }
        
        val nodeTranscriber = ADFNodeTranscriber(nodeMap)
        val startOrder = input.attrs?.order ?: 1
        val markdown = content.mapIndexed { index, item ->
            val itemNumber = startOrder + index
            val itemContent = nodeTranscriber.transcribeBlock(item).content
            "$itemNumber. $itemContent"
        }.joinToString("\n")
        
        return TranscribeResult("$markdown\n\n")
    }
}


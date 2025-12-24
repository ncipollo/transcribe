package transcribe.atlassian

import data.atlassian.adf.ADFBlockNode
import data.atlassian.adf.ADFInlineNode
import data.atlassian.adf.ADFNode
import kotlin.reflect.KClass
import transcribe.TranscribeResult

/**
 * Dispatcher that routes ADF nodes to their appropriate transcribers.
 * This is used by block nodes to transcribe their child content.
 */
class ADFNodeTranscriber(
    private val nodeMap: Map<KClass<out ADFNode>, ADFTranscriber<*>>
) {
    /**
     * Transcribes an inline node to markdown string.
     */
    fun transcribeInline(node: ADFInlineNode): TranscribeResult<String> {
        val transcriber = nodeMap[node::class] as? ADFTranscriber<ADFInlineNode>
        return transcriber?.transcribe(node) ?: TranscribeResult("")
    }

    /**
     * Transcribes a block node to markdown string.
     */
    fun transcribeBlock(node: ADFBlockNode): TranscribeResult<String> {
        val transcriber = nodeMap[node::class] as? ADFTranscriber<ADFBlockNode>
        return transcriber?.transcribe(node) ?: TranscribeResult("")
    }

    /**
     * Transcribes any ADF node to markdown string.
     */
    fun transcribe(node: ADFNode): TranscribeResult<String> {
        return when (node) {
            is ADFInlineNode -> transcribeInline(node)
            is ADFBlockNode -> transcribeBlock(node)
        }
    }
}


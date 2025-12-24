package transcribe.atlassian

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
     * Transcribes any ADF node to markdown string.
     */
    @Suppress("UNCHECKED_CAST")
    fun transcribe(node: ADFNode): TranscribeResult<String> {
        val transcriber = nodeMap[node::class] as? ADFTranscriber<ADFNode>
        return transcriber?.transcribe(node) ?: TranscribeResult("")
    }
}


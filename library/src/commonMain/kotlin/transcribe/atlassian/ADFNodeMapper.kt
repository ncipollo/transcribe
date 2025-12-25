package transcribe.atlassian

import data.atlassian.adf.ADFNode
import kotlin.reflect.KClass

/**
 * Mapper that provides transcribers for ADF nodes using factory functions.
 * This allows transcribers to be created with the final merged mapper,
 * ensuring custom overrides propagate correctly to container transcribers.
 */
class ADFNodeMapper(
    private val transcriberFactories: Map<KClass<out ADFNode>, (ADFNodeMapper) -> ADFTranscriber<*>>,
) {
    fun transcriberFor(node: ADFNode): ADFTranscriber<*>? {
        val factory = transcriberFactories[node::class]
        return factory?.invoke(this)
    }

    operator fun plus(mapper: ADFNodeMapper): ADFNodeMapper = ADFNodeMapper(transcriberFactories + mapper.transcriberFactories)
}

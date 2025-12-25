package transcribe.atlassian

import data.atlassian.adf.ADFNode
import kotlin.reflect.KClass

/**
 * Builder for creating ADF node mappers with factory functions.
 */
class ADFNodeMapperBuilder : ADFTranscriberMapBuildable {
    @Suppress("PropertyName")
    val _factories = mutableMapOf<KClass<out ADFNode>, (ADFNodeMapper) -> ADFTranscriber<*>>()

    /**
     * Add a transcriber factory for a specific node type (reified generic).
     */
    inline fun <reified Node : ADFNode> add(noinline factory: (ADFNodeMapper) -> ADFTranscriber<*>): ADFNodeMapperBuilder {
        return add(Node::class, factory)
    }

    /**
     * Add a transcriber factory with explicit KClass.
     */
    fun add(
        nodeClass: KClass<out ADFNode>,
        factory: (ADFNodeMapper) -> ADFTranscriber<*>,
    ): ADFNodeMapperBuilder {
        _factories[nodeClass] = factory
        return this
    }

    /**
     * Build the mapper from the configured factories.
     */
    fun mapper() = ADFNodeMapper(_factories)

    /**
     * Build and return the mapper (implements ADFTranscriberMapBuildable).
     */
    override fun build(): ADFNodeMapper = mapper()
}

/**
 * DSL function for building ADF node mappers.
 */
inline fun adfNodeMapper(block: ADFNodeMapperBuilder.() -> Unit): ADFNodeMapper = ADFNodeMapperBuilder().apply(block).mapper()

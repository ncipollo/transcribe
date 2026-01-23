package io.ncipollo.transcribe.transcriber.atlassian

/**
 * Builder for creating extension mappers with factory functions.
 */
class ExtensionMapperBuilder : ExtensionTranscriberMapBuildable {
    private val factories = mutableMapOf<String, (ExtensionMapper) -> ADFTranscriber<*>>()

    /**
     * Add a transcriber factory for a specific extension type.
     */
    fun add(
        extensionType: String,
        factory: (ExtensionMapper) -> ADFTranscriber<*>,
    ): ExtensionMapperBuilder {
        factories[extensionType] = factory
        return this
    }

    /**
     * Build the mapper from the configured factories.
     */
    fun mapper() = ExtensionMapper(factories)

    /**
     * Build and return the mapper (implements ExtensionTranscriberMapBuildable).
     */
    override fun build(): ExtensionMapper = mapper()
}

/**
 * DSL function for building extension mappers.
 */
inline fun extensionMapper(block: ExtensionMapperBuilder.() -> Unit): ExtensionMapper = ExtensionMapperBuilder().apply(block).mapper()

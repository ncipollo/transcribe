package transcribe.atlassian

/**
 * JVM-specific wrapper around ExtensionMapperBuilder that provides
 * convenience methods for working with extension mappers.
 */
class JvmExtensionMapperBuilder(
    private val builder: ExtensionMapperBuilder = ExtensionMapperBuilder(),
) : ExtensionTranscriberMapBuildable {
    /**
     * Add a transcriber factory for a specific extension type.
     */
    fun add(
        extensionType: String,
        factory: (ExtensionMapper) -> ADFTranscriber<*>,
    ): JvmExtensionMapperBuilder {
        builder.add(extensionType, factory)
        return this
    }

    /**
     * Build and return the mapper.
     */
    override fun build(): ExtensionMapper {
        return builder.build()
    }
}

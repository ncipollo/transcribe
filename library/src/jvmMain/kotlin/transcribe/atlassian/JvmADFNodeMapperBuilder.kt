package transcribe.atlassian

import data.atlassian.adf.ADFNode

/**
 * JVM-specific wrapper around ADFNodeMapperBuilder that provides
 * convenience methods for working with Java Class objects.
 */
class JvmADFNodeMapperBuilder(
    private val builder: ADFNodeMapperBuilder = ADFNodeMapperBuilder(),
) : ADFTranscriberMapBuildable {
    /**
     * Add a transcriber factory using Java Class (JVM).
     * Converts the Java Class to KClass before delegating to the builder.
     */
    fun add(
        nodeClass: Class<out ADFNode>,
        factory: (ADFNodeMapper) -> ADFTranscriber<*>,
    ): JvmADFNodeMapperBuilder {
        builder.add(nodeClass.kotlin, factory)
        return this
    }

    /**
     * Build and return the mapper.
     */
    override fun build(): ADFNodeMapper {
        return builder.build()
    }
}

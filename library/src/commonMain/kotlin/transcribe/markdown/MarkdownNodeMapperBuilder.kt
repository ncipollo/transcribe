package transcribe.markdown

import org.intellij.markdown.IElementType

/**
 * Builder for creating markdown node mappers with factory functions.
 */
class MarkdownNodeMapperBuilder {
    private val factories = mutableMapOf<IElementType, (MarkdownNodeMapper) -> MarkdownTranscriber<*>>()

    /**
     * Add a transcriber factory for a specific element type.
     */
    fun add(
        elementType: IElementType,
        factory: (MarkdownNodeMapper) -> MarkdownTranscriber<*>,
    ): MarkdownNodeMapperBuilder {
        factories[elementType] = factory
        return this
    }

    /**
     * Build the mapper from the configured factories.
     */
    fun mapper() = MarkdownNodeMapper(factories)
}

/**
 * DSL function for building markdown node mappers.
 */
fun markdownNodeMapper(block: MarkdownNodeMapperBuilder.() -> Unit): MarkdownNodeMapper =
    MarkdownNodeMapperBuilder().apply(block).mapper()


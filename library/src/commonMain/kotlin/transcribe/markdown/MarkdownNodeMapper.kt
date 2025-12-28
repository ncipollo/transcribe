package transcribe.markdown

import org.intellij.markdown.IElementType

/**
 * Mapper that provides transcribers for markdown element types using factory functions.
 * This allows transcribers to be created with the final merged mapper,
 * ensuring custom overrides propagate correctly to container transcribers.
 */
class MarkdownNodeMapper(
    private val transcriberFactories: Map<String, (MarkdownNodeMapper) -> MarkdownTranscriber<*>>,
) {
    fun transcriberFor(elementType: IElementType): MarkdownTranscriber<*>? {
        val factory = transcriberFactories[elementType.name]
        return factory?.invoke(this)
    }

    operator fun plus(mapper: MarkdownNodeMapper): MarkdownNodeMapper =
        MarkdownNodeMapper(transcriberFactories + mapper.transcriberFactories)
}


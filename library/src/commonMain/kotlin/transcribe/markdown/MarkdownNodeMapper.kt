package transcribe.markdown

import data.atlassian.adf.ADFBlockNode
import org.intellij.markdown.IElementType
import org.intellij.markdown.ast.ASTNode

/**
 * Mapper that provides transcribers for markdown element types using factory functions.
 * This allows transcribers to be created with the final merged mapper,
 * ensuring custom overrides propagate correctly to container transcribers.
 */
class MarkdownNodeMapper(
    private val transcriberFactories: Map<IElementType, (MarkdownNodeMapper) -> MarkdownTranscriber<*>>,
) {
    fun transcriberFor(elementType: IElementType): MarkdownTranscriber<*>? {
        val factory = transcriberFactories[elementType]
        return factory?.invoke(this)
    }

    /**
     * Transcribes block-level children of the given AST node using the appropriate transcribers.
     * Returns a list of ADF block nodes representing the transcribed children.
     */
    fun transcribeBlockChildren(
        parent: ASTNode,
        context: MarkdownContext,
    ): List<ADFBlockNode> {
        val result = mutableListOf<ADFBlockNode>()

        // Process children in order
        for (child in parent.children) {
            val transcriber = transcriberFor(child.type)
            if (transcriber != null) {
                @Suppress("UNCHECKED_CAST")
                val blockTranscriber = transcriber as? MarkdownTranscriber<ADFBlockNode>
                blockTranscriber?.transcribe(child, context)?.content?.let { result.add(it) }
            }
        }

        return result
    }

    operator fun plus(mapper: MarkdownNodeMapper): MarkdownNodeMapper =
        MarkdownNodeMapper(transcriberFactories + mapper.transcriberFactories)
}


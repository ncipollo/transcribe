package transcribe.markdown

import data.atlassian.adf.ADFBlockNode
import data.atlassian.adf.ADFInlineNode
import data.atlassian.adf.TextNode
import data.markdown.parser.getTextContent
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

    /**
     * Transcribes inline-level children of the given AST node using the appropriate transcribers.
     * Returns a list of ADF inline nodes representing the transcribed children.
     */
    fun transcribeInlineChildren(
        parent: ASTNode,
        context: MarkdownContext,
    ): List<ADFInlineNode> {
        return transcribeInlineChildren(parent.children, context)
    }

    /**
     * Transcribes a list of AST nodes as inline-level content using the appropriate transcribers.
     * Returns a list of ADF inline nodes representing the transcribed children.
     */
    fun transcribeInlineChildren(
        children: List<ASTNode>,
        context: MarkdownContext,
    ): List<ADFInlineNode> {
        val result = mutableListOf<ADFInlineNode>()

        // Process children in order
        for (child in children) {
            val transcriber = transcriberFor(child.type)
            if (transcriber != null) {
                @Suppress("UNCHECKED_CAST")
                val inlineTranscriber = transcriber as? MarkdownTranscriber<ADFInlineNode>
                inlineTranscriber?.transcribe(child, context)?.content?.let { result.add(it) }
            } else {
                // For unknown types, try to extract text content
                val textContent = child.getTextContent(context.markdownText).toString().trim()
                if (textContent.isNotEmpty()) {
                    result.add(TextNode(text = textContent))
                }
            }
        }

        return result
    }

    operator fun plus(mapper: MarkdownNodeMapper): MarkdownNodeMapper =
        MarkdownNodeMapper(transcriberFactories + mapper.transcriberFactories)
}

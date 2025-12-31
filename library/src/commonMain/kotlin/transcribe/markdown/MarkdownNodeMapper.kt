package transcribe.markdown

import data.atlassian.adf.ADFBlockNode
import data.atlassian.adf.ADFInlineNode
import data.atlassian.adf.HardBreakNode
import data.atlassian.adf.ParagraphNode
import data.atlassian.adf.TextNode
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
                when (val transcribedNode = transcriber.transcribe(child, context).content) {
                    is ADFBlockNode -> result.add(transcribedNode)
                    is ADFInlineNode -> {
                        // Skip wrapping newline-only elements
                        if (!isNewlineOnly(transcribedNode)) {
                            result.add(ParagraphNode(content = listOf(transcribedNode)))
                        }
                    }
                }
            }
        }

        return result
    }

    /**
     * Checks if an inline node contains only newline content.
     * Returns true for HardBreakNode or TextNode containing only whitespace/newlines.
     */
    private fun isNewlineOnly(node: ADFInlineNode): Boolean =
        when (node) {
            is HardBreakNode -> true
            is TextNode -> node.text.all { it.isWhitespace() }
            else -> false
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
                val transcribedNode = transcriber.transcribe(child, context)?.content
                if (transcribedNode is ADFInlineNode) {
                    result.add(transcribedNode)
                }
            }
        }

        return result
    }

    operator fun plus(mapper: MarkdownNodeMapper): MarkdownNodeMapper =
        MarkdownNodeMapper(transcriberFactories + mapper.transcriberFactories)
}

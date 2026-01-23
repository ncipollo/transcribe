package io.ncipollo.transcribe.transcriber.markdown

import io.ncipollo.transcribe.context.MarkdownContext
import io.ncipollo.transcribe.data.atlassian.adf.ADFBlockNode
import io.ncipollo.transcribe.data.atlassian.adf.ADFInlineNode
import io.ncipollo.transcribe.data.atlassian.adf.HardBreakNode
import io.ncipollo.transcribe.data.atlassian.adf.ParagraphNode
import io.ncipollo.transcribe.data.atlassian.adf.TextNode
import org.intellij.markdown.IElementType
import org.intellij.markdown.ast.ASTNode
import io.ncipollo.transcribe.transcriber.TranscribeResult
import io.ncipollo.transcribe.transcriber.action.TranscriberAction

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
     * Returns a TranscribeResult containing a list of ADF block nodes representing the transcribed children and aggregated actions.
     */
    fun transcribeBlockChildren(
        parent: ASTNode,
        context: MarkdownContext,
    ): TranscribeResult<List<ADFBlockNode>> {
        val result = mutableListOf<ADFBlockNode>()
        val actions = mutableListOf<TranscriberAction>()

        // Process children in order
        for (child in parent.children) {
            val transcriber = transcriberFor(child.type)
            if (transcriber != null) {
                val transcribeResult = transcriber.transcribe(child, context)
                actions.addAll(transcribeResult.actions)
                when (val transcribedNode = transcribeResult.content) {
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

        return TranscribeResult(result, actions)
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
     * Returns a TranscribeResult containing a list of ADF inline nodes representing the transcribed children and aggregated actions.
     */
    fun transcribeInlineChildren(
        parent: ASTNode,
        context: MarkdownContext,
    ): TranscribeResult<List<ADFInlineNode>> {
        return transcribeInlineChildren(parent.children, context)
    }

    /**
     * Transcribes a list of AST nodes as inline-level content using the appropriate transcribers.
     * Returns a TranscribeResult containing a list of ADF inline nodes representing the transcribed children and aggregated actions.
     */
    fun transcribeInlineChildren(
        children: List<ASTNode>,
        context: MarkdownContext,
    ): TranscribeResult<List<ADFInlineNode>> {
        val result = mutableListOf<ADFInlineNode>()
        val actions = mutableListOf<TranscriberAction>()

        // Process children in order
        for (child in children) {
            val transcriber = transcriberFor(child.type)
            if (transcriber != null) {
                val transcribeResult = transcriber.transcribe(child, context)
                actions.addAll(transcribeResult.actions)
                val transcribedNode = transcribeResult.content
                if (transcribedNode is ADFInlineNode) {
                    result.add(transcribedNode)
                }
            }
        }

        return TranscribeResult(result, actions)
    }

    operator fun plus(mapper: MarkdownNodeMapper): MarkdownNodeMapper =
        MarkdownNodeMapper(transcriberFactories + mapper.transcriberFactories)
}

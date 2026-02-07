package io.ncipollo.transcribe.transcriber.markdown

import io.ncipollo.transcribe.context.MarkdownContext
import io.ncipollo.transcribe.data.atlassian.adf.*
import io.ncipollo.transcribe.data.markdown.parser.getTextContent
import io.ncipollo.transcribe.transcriber.TranscribeResult
import io.ncipollo.transcribe.transcriber.action.TranscriberAction
import org.intellij.markdown.IElementType
import org.intellij.markdown.MarkdownElementTypes
import org.intellij.markdown.MarkdownTokenTypes
import org.intellij.markdown.ast.ASTNode
import org.intellij.markdown.flavours.gfm.GFMElementTypes

/**
 * Helper class for transcribing inline nodes with mark accumulation.
 * This flattens nested inline formatting nodes (like nested bold/italic) into a flat list
 * of TextNodes where marks are accumulated from parent to child nodes.
 *
 * For example: "_italic with **bold** text_" becomes:
 * [
 *   TextNode("italic with ", marks=[EmMark]),
 *   TextNode("bold ", marks=[EmMark, StrongMark]),
 *   TextNode("text", marks=[EmMark])
 * ]
 */
class InlineMarkAccumulator(
    private val mapper: MarkdownNodeMapper
) {
    /**
     * Transcribes inline children with mark accumulation for nested formatting.
     */
    fun transcribeWithMarks(
        parent: ASTNode,
        context: MarkdownContext,
        accumulatedMarks: List<ADFMark> = emptyList()
    ): TranscribeResult<List<ADFInlineNode>> {
        return transcribeWithMarks(parent.children, context, accumulatedMarks)
    }

    /**
     * Transcribes a list of child nodes with mark accumulation.
     */
    fun transcribeWithMarks(
        children: List<ASTNode>,
        context: MarkdownContext,
        accumulatedMarks: List<ADFMark> = emptyList()
    ): TranscribeResult<List<ADFInlineNode>> {
        val result = mutableListOf<ADFInlineNode>()
        val actions = mutableListOf<TranscriberAction>()

        for (child in children) {
            when {
                // Mark-generating nodes: accumulate mark and recurse
                // Skip delimiter nodes (they have the same type but no children)
                isMarkGeneratingNode(child.type) -> {
                    if (child.children.isNotEmpty()) {
                        val mark = markForNodeType(child.type)!!
                        val newMarks = accumulatedMarks + mark
                        val childResult = transcribeWithMarks(
                            child.children,
                            context,
                            newMarks
                        )
                        result.addAll(childResult.content)
                        actions.addAll(childResult.actions)
                    }
                    // If no children, it's a delimiter token - skip it
                }

                // Leaf text nodes: create TextNode with accumulated marks
                // BUT: check for custom transcribers first to allow overriding
                isLeafInlineNode(child.type) -> {
                    val transcriber = mapper.transcriberFor(child.type)
                    if (transcriber != null) {
                        // Use custom transcriber if one exists
                        val transcribeResult = transcriber.transcribe(child, context)
                        actions.addAll(transcribeResult.actions)
                        if (transcribeResult.content is ADFInlineNode) {
                            // Apply accumulated marks to the result
                            val node = transcribeResult.content
                            if (node is TextNode && accumulatedMarks.isNotEmpty()) {
                                result.add(node.copy(marks = (node.marks ?: emptyList()) + accumulatedMarks))
                            } else {
                                result.add(node)
                            }
                        }
                    } else {
                        // No custom transcriber, create TextNode directly
                        val text = child.getTextContent(context.markdownText).toString()
                        if (text.isNotEmpty()) {
                            result.add(TextNode(
                                text = text,
                                marks = accumulatedMarks.ifEmpty { null }
                            ))
                        }
                    }
                }

                // Special inline nodes: use existing transcribers
                else -> {
                    val transcriber = mapper.transcriberFor(child.type)
                    if (transcriber != null) {
                        val transcribeResult = transcriber.transcribe(child, context)
                        actions.addAll(transcribeResult.actions)
                        if (transcribeResult.content is ADFInlineNode) {
                            result.add(transcribeResult.content)
                        }
                    }
                }
            }
        }

        return TranscribeResult(result, actions)
    }

    /**
     * Determines which mark (if any) a markdown node type represents.
     */
    private fun markForNodeType(type: IElementType): ADFMark? =
        when (type) {
            MarkdownElementTypes.EMPH -> EmMark
            MarkdownElementTypes.STRONG -> StrongMark
            GFMElementTypes.STRIKETHROUGH -> StrikeMark
            else -> null
        }

    /**
     * Checks if a node type generates a mark that should be accumulated.
     */
    private fun isMarkGeneratingNode(type: IElementType): Boolean =
        markForNodeType(type) != null

    /**
     * Checks if a node type is a leaf inline node (text, whitespace).
     * Note: EOL is NOT included here because it has its own transcriber (EolTranscriber)
     * that converts it to HardBreakNode.
     */
    private fun isLeafInlineNode(type: IElementType): Boolean =
        type in setOf(
            MarkdownTokenTypes.TEXT,
            MarkdownTokenTypes.WHITE_SPACE
        )
}

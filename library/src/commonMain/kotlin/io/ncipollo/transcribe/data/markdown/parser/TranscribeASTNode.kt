package io.ncipollo.transcribe.data.markdown.parser

import org.intellij.markdown.IElementType
import org.intellij.markdown.ast.ASTNode

/**
 * An immutable implementation of ASTNode that can be used for creating modified AST trees.
 * Useful for preprocessing operations that need to restructure the AST.
 */
data class TranscribeASTNode(
    override val type: IElementType,
    override val children: List<ASTNode>,
    override val startOffset: Int,
    override val endOffset: Int,
    override val parent: ASTNode? = null,
) : ASTNode

/**
 * Converts any ASTNode to a TranscribeASTNode, recursively converting all children.
 */
fun ASTNode.toTranscribeNode(): TranscribeASTNode {
    return TranscribeASTNode(
        type = type,
        children = children.map { it.toTranscribeNode() },
        startOffset = startOffset,
        endOffset = endOffset,
        parent = parent,
    )
}

/**
 * Creates a copy of this ASTNode with new children.
 */
fun ASTNode.copyWithChildren(newChildren: List<ASTNode>): TranscribeASTNode {
    return TranscribeASTNode(
        type = type,
        children = newChildren,
        startOffset = startOffset,
        endOffset = endOffset,
        parent = parent,
    )
}

/**
 * Creates a copy of this ASTNode with new start and end offsets.
 */
fun ASTNode.copyWithOffsets(newStart: Int, newEnd: Int): TranscribeASTNode {
    return TranscribeASTNode(
        type = type,
        children = children,
        startOffset = newStart,
        endOffset = newEnd,
        parent = parent,
    )
}

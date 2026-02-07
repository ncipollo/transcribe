package io.ncipollo.transcribe.data.markdown.parser

import org.intellij.markdown.IElementType
import org.intellij.markdown.MarkdownElementTypes
import org.intellij.markdown.MarkdownTokenTypes
import org.intellij.markdown.ast.ASTNode

fun ASTNode.getTextContent(markdownText: CharSequence): CharSequence {
    return markdownText.subSequence(startOffset, endOffset)
}

/**
 * Extracts text content from a node and strips markdown delimiters.
 * @param markdownText The full markdown text
 * @param prefix The prefix delimiter to remove (e.g., "**", "*", "~~", "[")
 * @param suffix The suffix delimiter to remove (defaults to prefix if not specified)
 */
fun ASTNode.getTextContentWithoutDelimiters(
    markdownText: CharSequence,
    prefix: String,
    suffix: String = prefix
): String {
    return getTextContent(markdownText).toString().removePrefix(prefix).removeSuffix(suffix)
}

/**
 * Traverses the subtree with early exit support.
 * @param visitor returns true to continue traversal, false to stop.
 * @return true if traversal completed, false if stopped early.
 */
fun ASTNode.traverse(
    depth: Int = 0,
    visitor: (node: ASTNode, depth: Int) -> Boolean,
): Boolean {
    if (!visitor(this, depth)) return false
    for (child in children) {
        if (!child.traverse(depth + 1, visitor)) return false
    }
    return true
}

/**
 * Finds the first child of the specified type anywhere in the subtree.
 */
fun ASTNode.findChildOfTypeInSubtree(type: IElementType): ASTNode? {
    var result: ASTNode? = null
    traverse { node, _ ->
        if (node.type == type) {
            result = node
            false // We found one, stop traversing
        } else {
            true
        }
    }
    return result
}

fun ASTNode.findImageNodes(): List<ASTNode> {
    val imageNodes = mutableListOf<ASTNode>()
    traverse { node, _ ->
        if (node.type == MarkdownElementTypes.IMAGE) {
            imageNodes.add(node)
        }
        true
    }
    return imageNodes
}

fun ASTNode.printTree() {
    traverse { node, depth ->
        val prefix = "-".repeat(depth)
        println("$prefix${node.type}")
        true
    }
}

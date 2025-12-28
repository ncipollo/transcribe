package data.markdown.parser

import org.intellij.markdown.MarkdownElementTypes
import org.intellij.markdown.MarkdownTokenTypes
import org.intellij.markdown.ast.ASTNode
import org.intellij.markdown.ast.findChildOfType

fun ASTNode.findTextContent(markdownText: String) =
    findChildOfType(MarkdownTokenTypes.TEXT)
        ?.getTextContent(markdownText)
        ?.toString() ?: ""

fun ASTNode.getTextContent(markdownText: CharSequence): CharSequence {
    return markdownText.subSequence(startOffset, endOffset)
}

fun ASTNode.traverse(
    depth: Int = 0,
    visitor: (node: ASTNode, depth: Int) -> Unit,
) {
    visitor(this, depth)
    children.forEach { child ->
        child.traverse(depth + 1, visitor)
    }
}

fun ASTNode.findImageNodes(): List<ASTNode> {
    val imageNodes = mutableListOf<ASTNode>()
    traverse { node, _ ->
        if (node.type == MarkdownElementTypes.IMAGE) {
            imageNodes.add(node)
        }
    }
    return imageNodes
}

fun ASTNode.printTree() {
    traverse { node, depth ->
        val prefix = "-".repeat(depth)
        println("$prefix${node.type}")
    }
}

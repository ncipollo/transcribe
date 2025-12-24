package data.markdown.parser

import org.intellij.markdown.ast.ASTNode
import org.intellij.markdown.IElementType
import org.intellij.markdown.MarkdownTokenTypes

fun ASTNode.findChildOfType(type: IElementType): ASTNode? {
    return children.firstOrNull { it.type == type }
}

fun ASTNode.getParentOfType(vararg types: IElementType): ASTNode? {
    var currentNode = parent
    while (currentNode != null && !types.contains(currentNode.type)) {
        currentNode = currentNode.parent
    }
    return currentNode
}

fun ASTNode.getTextContent(allFileText: CharSequence): CharSequence {
    return allFileText.subSequence(startOffset, endOffset)
}

fun ASTNode.findTextNode(markdownText: CharSequence): String {
    return findChildOfType(MarkdownTokenTypes.TEXT)
        ?.getTextContent(markdownText)
        ?.toString() ?: ""
}


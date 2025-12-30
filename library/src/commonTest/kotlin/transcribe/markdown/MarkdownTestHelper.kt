package transcribe.markdown

import data.markdown.parser.MarkdownDocument
import data.markdown.parser.findChildOfTypeInSubtree
import org.intellij.markdown.IElementType
import org.intellij.markdown.ast.ASTNode
import kotlin.test.assertNotNull

/**
 * Helper object for common markdown test utilities.
 */
object MarkdownTestHelper {
    /**
     * Finds and returns the first AST node of the specified element type from the given markdown text.
     * Searches through the root node's children to find a matching node.
     *
     * @param markdownText The markdown text to parse
     * @param elementType The element type to search for
     * @return The first matching AST node
     * @throws AssertionError if no matching node is found
     */
    fun findNode(
        markdownText: String,
        elementType: IElementType,
    ): ASTNode {
        val document = MarkdownDocument.create(markdownText)
        val node = document.rootNode.findChildOfTypeInSubtree(elementType)
        assertNotNull(node, "Should find ${elementType.name} node")
        return node
    }

    /**
     * Finds and returns the first AST node of the specified element type from nested children.
     * Searches through all descendants to find a matching node.
     *
     * @param markdownText The markdown text to parse
     * @param elementType The element type to search for
     * @return The first matching AST node
     * @throws AssertionError if no matching node is found
     */
    fun findNestedNode(
        markdownText: String,
        elementType: IElementType,
    ): ASTNode {
        val document = MarkdownDocument.create(markdownText)
        val node = findNodeRecursive(document.rootNode, elementType)
        assertNotNull(node, "Should find ${elementType.name} node")
        return node
    }

    private fun findNodeRecursive(
        node: ASTNode,
        elementType: IElementType,
    ): ASTNode? {
        if (node.type == elementType) {
            return node
        }
        for (child in node.children) {
            val found = findNodeRecursive(child, elementType)
            if (found != null) {
                return found
            }
        }
        return null
    }
}

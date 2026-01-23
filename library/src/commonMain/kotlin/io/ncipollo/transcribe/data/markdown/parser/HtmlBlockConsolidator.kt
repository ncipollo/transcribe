package io.ncipollo.transcribe.data.markdown.parser

import org.intellij.markdown.MarkdownElementTypes
import org.intellij.markdown.ast.ASTNode

/**
 * Consolidates split HTML blocks in a Markdown AST tree.
 *
 * The Markdown parser splits HTML blocks when they contain blank lines, breaking elements
 * like `<details>...</details>` into multiple HTML_BLOCK nodes with intervening content.
 * This consolidator detects such splits and folds them back into single HTML_BLOCK nodes.
 */
object HtmlBlockConsolidator {
    /**
     * Consolidates split HTML blocks in the AST tree.
     *
     * @param rootNode The root AST node to process
     * @param markdownText The markdown text to extract node content
     * @return A new root node with consolidated HTML blocks
     */
    fun consolidate(rootNode: ASTNode, markdownText: String): ASTNode {
        val consolidatedChildren = consolidateChildren(rootNode.children.toList(), markdownText)
        return rootNode.copyWithChildren(consolidatedChildren)
    }

    /**
     * Consolidates HTML blocks in a list of sibling nodes.
     */
    private fun consolidateChildren(children: List<ASTNode>, markdownText: String): List<ASTNode> {
        val result = mutableListOf<ASTNode>()
        var currentIndex = 0

        while (currentIndex < children.size) {
            val child = children[currentIndex]

            // Check if this is an HTML_BLOCK that needs consolidation
            if (child.type == MarkdownElementTypes.HTML_BLOCK) {
                val consolidation = tryConsolidate(child, children, currentIndex, markdownText)
                if (consolidation != null) {
                    // Add the consolidated node and skip the folded siblings
                    result.add(consolidation.consolidatedNode)
                    currentIndex += consolidation.skipCount + 1
                    continue
                }
            }

            // Recursively process children of this node
            if (child.children.isNotEmpty()) {
                val consolidatedGrandchildren = consolidateChildren(child.children.toList(), markdownText)
                result.add(child.copyWithChildren(consolidatedGrandchildren))
            } else {
                result.add(child)
            }

            currentIndex++
        }

        return result
    }

    /**
     * Attempts to consolidate an HTML_BLOCK with subsequent siblings.
     * Returns null if the block doesn't need consolidation.
     */
    private fun tryConsolidate(
        htmlBlock: ASTNode,
        siblings: List<ASTNode>,
        nodeIndex: Int,
        markdownText: String,
    ): ConsolidationResult? {
        // Extract HTML content to check for unclosed tags
        val htmlText = htmlBlock.getTextContent(markdownText).toString()
        val unclosedTag = findUnclosedTag(htmlText) ?: return null

        // Look for the matching closing HTML_BLOCK
        var closingIndex = -1
        for (siblingIndex in (nodeIndex + 1) until siblings.size) {
            val sibling = siblings[siblingIndex]
            if (sibling.type == MarkdownElementTypes.HTML_BLOCK) {
                val siblingText = sibling.getTextContent(markdownText).toString()
                if (containsClosingTag(siblingText, unclosedTag)) {
                    closingIndex = siblingIndex
                    break
                }
            }
        }

        if (closingIndex == -1) return null

        val closingBlock = siblings[closingIndex]

        // Create a consolidated HTML_BLOCK that spans from the opening to the closing block
        val consolidatedNode = TranscribeASTNode(
            type = MarkdownElementTypes.HTML_BLOCK,
            children = emptyList(),
            startOffset = htmlBlock.startOffset,
            endOffset = closingBlock.endOffset,
            parent = htmlBlock.parent,
        )

        return ConsolidationResult(
            consolidatedNode = consolidatedNode,
            skipCount = closingIndex - nodeIndex,
        )
    }

    /**
     * Finds the first unclosed HTML tag in the given HTML text.
     * Returns the tag name if found, null otherwise.
     */
    private fun findUnclosedTag(htmlText: String): String? {
        val blockElements = setOf("details", "div", "section", "article", "header", "footer", "nav")
        val tagStack = mutableListOf<String>()
        val tagRegex = Regex("<(/?)([a-zA-Z][a-zA-Z0-9]*)[^>]*>", RegexOption.IGNORE_CASE)

        tagRegex.findAll(htmlText).forEach { match ->
            val isClosing = match.groupValues[1] == "/"
            val tagName = match.groupValues[2].lowercase()

            if (tagName !in blockElements) return@forEach

            if (isClosing) {
                // Remove matching opening tag from stack
                val lastIndex = tagStack.lastIndexOf(tagName)
                if (lastIndex >= 0) {
                    tagStack.removeAt(lastIndex)
                }
            } else {
                // Self-closing tags like <br/> don't need closing
                val fullMatch = match.value
                if (!fullMatch.endsWith("/>")) {
                    tagStack.add(tagName)
                }
            }
        }

        // Return the first unclosed tag, if any
        return tagStack.firstOrNull()
    }

    /**
     * Checks if the given HTML text contains a closing tag for the specified tag name.
     */
    private fun containsClosingTag(htmlText: String, tagName: String): Boolean {
        return htmlText.contains("</$tagName>", ignoreCase = true)
    }

    private data class ConsolidationResult(
        val consolidatedNode: ASTNode,
        val skipCount: Int,
    )
}

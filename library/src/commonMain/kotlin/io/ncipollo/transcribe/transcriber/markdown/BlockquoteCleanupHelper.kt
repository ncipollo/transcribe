package io.ncipollo.transcribe.transcriber.markdown

import io.ncipollo.transcribe.data.atlassian.adf.ADFBlockNode
import io.ncipollo.transcribe.data.atlassian.adf.ADFInlineNode
import io.ncipollo.transcribe.data.atlassian.adf.HardBreakNode
import io.ncipollo.transcribe.data.atlassian.adf.ParagraphNode
import io.ncipollo.transcribe.data.atlassian.adf.TextNode

/**
 * Helper object for cleaning up blockquote content artifacts.
 * Removes junk TextNodes that appear after HardBreakNodes and empty blockquote marker paragraphs.
 */
object BlockquoteCleanupHelper {
    /**
     * Cleans up artifacts in blockquote content:
     * 1. Removes TextNodes after HardBreakNodes if they contain only " " or ">" or "> "
     * 2. Removes ParagraphNodes that contain only a single TextNode with text ">" or "> "
     */
    fun cleanupBlockContent(content: List<ADFBlockNode>): List<ADFBlockNode> {
        return content
            .map { if (it is ParagraphNode) cleanupParagraphInlineContent(it) else it }
            .filter { !isEmptyBlockquoteParagraph(it) }
    }

    /**
     * Cleans inline content in a paragraph by removing TextNodes that appear after HardBreakNodes
     * if they contain only a single space " ", ">", or "> ".
     */
    private fun cleanupParagraphInlineContent(paragraph: ParagraphNode): ParagraphNode {
        val inlineContent = paragraph.content ?: return paragraph
        val cleanedContent = mutableListOf<ADFInlineNode>()

        var ii = 0
        while (ii < inlineContent.size) {
            val currentNode = inlineContent[ii]
            cleanedContent.add(currentNode)

            // If the current node is a HardBreakNode, check if the next node should be removed
            if (currentNode is HardBreakNode && ii + 1 < inlineContent.size) {
                val nextNode = inlineContent[ii + 1]
                if (nextNode is TextNode && (nextNode.text == " " || isBlockquoteMarkerText(nextNode))) {
                    // Skip the next node (don't add it to cleanedContent)
                    ii += 2
                    continue
                }
            }
            ii++
        }

        return ParagraphNode(
            content = cleanedContent,
            marks = paragraph.marks,
            attrs = paragraph.attrs,
        )
    }

    /**
     * Checks if a TextNode contains only a blockquote marker ">" (possibly with whitespace).
     */
    private fun isBlockquoteMarkerText(textNode: TextNode): Boolean {
        return textNode.text.trim() == ">"
    }

    /**
     * Checks if a block node is a paragraph containing only a single TextNode with text ">" or "> ".
     */
    private fun isEmptyBlockquoteParagraph(node: ADFBlockNode): Boolean {
        if (node !is ParagraphNode) return false
        val content = node.content ?: return false
        return content.size == 1 &&
            content[0] is TextNode &&
            isBlockquoteMarkerText(content[0] as TextNode)
    }
}

package transcribe.markdown

import data.atlassian.adf.ADFBlockNode
import data.atlassian.adf.ADFInlineNode
import data.atlassian.adf.BlockquoteNode
import data.atlassian.adf.HardBreakNode
import data.atlassian.adf.ParagraphNode
import data.atlassian.adf.TextNode
import org.intellij.markdown.ast.ASTNode
import transcribe.TranscribeResult

/**
 * Transcriber for BLOCK_QUOTE nodes that converts markdown blockquotes to ADF BlockquoteNode.
 */
class BlockquoteTranscriber(
    private val nodeMapper: MarkdownNodeMapper,
) : MarkdownTranscriber<BlockquoteNode> {
    override fun transcribe(
        input: ASTNode,
        context: MarkdownContext,
    ): TranscribeResult<BlockquoteNode> {
        val blockContent = nodeMapper.transcribeBlockChildren(input, context)
        val cleanedContent = cleanupBlockContent(blockContent)
        return TranscribeResult(
            BlockquoteNode(
                content = cleanedContent,
            ),
        )
    }

    /**
     * Cleans up artifacts in blockquote content:
     * 1. Removes TextNodes after HardBreakNodes if they contain only " " or ">" or "> "
     * 2. Removes ParagraphNodes that contain only a single TextNode with text ">" or "> "
     */
    private fun cleanupBlockContent(content: List<ADFBlockNode>): List<ADFBlockNode> {
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
                if (nextNode is TextNode && (nextNode.text == " " || nextNode.text.trim() == ">")) {
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
     * Checks if a block node is a paragraph containing only a single TextNode with text ">" or "> ".
     */
    private fun isEmptyBlockquoteParagraph(node: ADFBlockNode): Boolean {
        if (node !is ParagraphNode) return false
        val content = node.content ?: return false
        return content.size == 1 &&
            content[0] is TextNode &&
            ((content[0] as TextNode).text == ">" || (content[0] as TextNode).text == "> ")
    }
}

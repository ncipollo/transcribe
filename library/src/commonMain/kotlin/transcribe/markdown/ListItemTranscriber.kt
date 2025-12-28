package transcribe.markdown

import data.atlassian.adf.ADFNode
import data.atlassian.adf.ListItemNode
import org.intellij.markdown.MarkdownElementTypes
import org.intellij.markdown.MarkdownTokenTypes
import org.intellij.markdown.ast.ASTNode
import org.intellij.markdown.flavours.gfm.GFMElementTypes
import org.intellij.markdown.flavours.gfm.GFMTokenTypes
import transcribe.TranscribeResult

/**
 * Transcriber for LIST_ITEM nodes that converts markdown list items to ADF ListItemNode.
 */
class ListItemTranscriber(
    private val blockTranscriber: BlockContentTranscriber,
) : MarkdownTranscriber<ListItemNode> {
    override fun transcribe(
        input: ASTNode,
        context: MarkdownContext,
    ): TranscribeResult<ListItemNode> {
        // List items can contain paragraphs, nested lists, etc.
        // Skip LIST_BULLET, LIST_NUMBER, and CHECK_BOX nodes
        val contentNodes = input.children.filter { child ->
            child.type != MarkdownTokenTypes.LIST_BULLET &&
                child.type != MarkdownTokenTypes.LIST_NUMBER &&
                child.type != GFMTokenTypes.CHECK_BOX
        }

        val content = mutableListOf<ADFNode>()

        for (child in contentNodes) {
            when (child.type) {
                MarkdownElementTypes.PARAGRAPH -> {
                    val paragraphTranscriber = ParagraphTranscriber(blockTranscriber.inlineTranscriber)
                    paragraphTranscriber.transcribe(child, context).content?.let { content.add(it) }
                }
                MarkdownElementTypes.UNORDERED_LIST -> {
                    val bulletListTranscriber = BulletListTranscriber(blockTranscriber)
                    bulletListTranscriber.transcribe(child, context).content?.let { content.add(it) }
                }
                MarkdownElementTypes.ORDERED_LIST -> {
                    val orderedListTranscriber = OrderedListTranscriber(blockTranscriber)
                    orderedListTranscriber.transcribe(child, context).content?.let { content.add(it) }
                }
                MarkdownTokenTypes.EOL -> {
                    // Skip end-of-line nodes
                }
                else -> {
                    // Try to transcribe as block content
                    val blockContent = blockTranscriber.transcribeChildren(child, context)
                    content.addAll(blockContent)
                }
            }
        }

        return TranscribeResult(
            ListItemNode(
                content = content,
            ),
        )
    }
}


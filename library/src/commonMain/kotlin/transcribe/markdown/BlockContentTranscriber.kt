package transcribe.markdown

import data.atlassian.adf.ADFBlockNode
import org.intellij.markdown.MarkdownElementTypes
import org.intellij.markdown.ast.ASTNode
import org.intellij.markdown.flavours.gfm.GFMElementTypes

/**
 * Helper class for transcribing block-level content from markdown AST nodes.
 * Handles paragraphs, headings, lists, code blocks, blockquotes, tables, etc.
 */
class BlockContentTranscriber(
    val inlineTranscriber: InlineContentTranscriber,
) {
    fun transcribeChildren(
        parent: ASTNode,
        context: MarkdownContext,
    ): List<ADFBlockNode> {
        val result = mutableListOf<ADFBlockNode>()

        // Process children in order
        for (child in parent.children) {
            when (child.type) {
                MarkdownElementTypes.PARAGRAPH -> {
                    val paragraphTranscriber = ParagraphTranscriber(inlineTranscriber)
                    paragraphTranscriber.transcribe(child, context).content?.let { result.add(it) }
                }
                MarkdownElementTypes.ATX_1,
                MarkdownElementTypes.ATX_2,
                MarkdownElementTypes.ATX_3,
                MarkdownElementTypes.ATX_4,
                MarkdownElementTypes.ATX_5,
                MarkdownElementTypes.ATX_6,
                MarkdownElementTypes.SETEXT_1,
                MarkdownElementTypes.SETEXT_2 -> {
                    val headingTranscriber = HeadingTranscriber(inlineTranscriber)
                    headingTranscriber.transcribe(child, context).content?.let { result.add(it) }
                }
                MarkdownElementTypes.UNORDERED_LIST -> {
                    val bulletListTranscriber = BulletListTranscriber(this)
                    bulletListTranscriber.transcribe(child, context).content?.let { result.add(it) }
                }
                MarkdownElementTypes.ORDERED_LIST -> {
                    val orderedListTranscriber = OrderedListTranscriber(this)
                    orderedListTranscriber.transcribe(child, context).content?.let { result.add(it) }
                }
                MarkdownElementTypes.BLOCK_QUOTE -> {
                    val blockquoteTranscriber = BlockquoteTranscriber(this)
                    blockquoteTranscriber.transcribe(child, context).content?.let { result.add(it) }
                }
                MarkdownElementTypes.CODE_FENCE,
                MarkdownElementTypes.CODE_BLOCK -> {
                    val codeBlockTranscriber = CodeBlockTranscriber()
                    codeBlockTranscriber.transcribe(child, context).content?.let { result.add(it) }
                }
                MarkdownElementTypes.IMAGE -> {
                    val imageTranscriber = ImageTranscriber(inlineTranscriber)
                    imageTranscriber.transcribe(child, context).content?.let { result.add(it) }
                }
                GFMElementTypes.TABLE -> {
                    val tableTranscriber = TableTranscriber(this)
                    tableTranscriber.transcribe(child, context).content?.let { result.add(it) }
                }
                else -> {
                    // Unknown block type - skip for now
                }
            }
        }

        return result
    }
}


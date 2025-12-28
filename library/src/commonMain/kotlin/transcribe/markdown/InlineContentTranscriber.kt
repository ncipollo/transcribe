package transcribe.markdown

import data.atlassian.adf.ADFInlineNode
import data.markdown.parser.getTextContent
import org.intellij.markdown.MarkdownElementTypes
import org.intellij.markdown.MarkdownTokenTypes
import org.intellij.markdown.ast.ASTNode
import org.intellij.markdown.flavours.gfm.GFMElementTypes

/**
 * Helper class for transcribing inline content from markdown AST nodes.
 * Handles nested inline elements like text, emphasis, strong, code spans, links, etc.
 */
class InlineContentTranscriber {
    fun transcribeChildren(
        parent: ASTNode,
        context: MarkdownContext,
    ): List<ADFInlineNode> {
        val result = mutableListOf<ADFInlineNode>()

        // Process children in order
        for (child in parent.children) {
            when (child.type) {
                MarkdownTokenTypes.TEXT -> {
                    val textTranscriber = TextTranscriber()
                    textTranscriber.transcribe(child, context).content?.let { result.add(it) }
                }
                MarkdownElementTypes.EMPH -> {
                    val emphasisTranscriber = EmphasisTranscriber(this)
                    emphasisTranscriber.transcribe(child, context).content?.let { result.add(it) }
                }
                MarkdownElementTypes.STRONG -> {
                    val strongTranscriber = StrongTranscriber(this)
                    strongTranscriber.transcribe(child, context).content?.let { result.add(it) }
                }
                MarkdownElementTypes.CODE_SPAN -> {
                    val codeSpanTranscriber = CodeSpanTranscriber()
                    codeSpanTranscriber.transcribe(child, context).content?.let { result.add(it) }
                }
                MarkdownElementTypes.INLINE_LINK,
                MarkdownElementTypes.FULL_REFERENCE_LINK,
                MarkdownElementTypes.SHORT_REFERENCE_LINK,
                MarkdownElementTypes.AUTOLINK -> {
                    val linkTranscriber = LinkTranscriber(this)
                    linkTranscriber.transcribe(child, context).content?.let { result.add(it) }
                }
                GFMElementTypes.STRIKETHROUGH -> {
                    val strikethroughTranscriber = StrikethroughTranscriber(this)
                    strikethroughTranscriber.transcribe(child, context).content?.let { result.add(it) }
                }
                // Skip whitespace-only nodes
                MarkdownTokenTypes.EOL -> {
                    // Skip end-of-line nodes
                }
                else -> {
                    // For unknown types, try to extract text content
                    val textContent = child.getTextContent(context.markdownText).toString().trim()
                    if (textContent.isNotEmpty()) {
                        result.add(data.atlassian.adf.TextNode(text = textContent))
                    }
                }
            }
        }

        return result
    }
}


package io.ncipollo.transcribe.transcriber.markdown

import io.ncipollo.transcribe.context.MarkdownContext
import io.ncipollo.transcribe.data.atlassian.adf.HeadingAttrs
import io.ncipollo.transcribe.data.atlassian.adf.HeadingNode
import org.intellij.markdown.MarkdownElementTypes
import org.intellij.markdown.MarkdownTokenTypes
import org.intellij.markdown.ast.ASTNode
import org.intellij.markdown.ast.findChildOfType
import io.ncipollo.transcribe.transcriber.TranscribeResult

/**
 * Transcriber for ATX_1-6 and SETEXT_1-2 heading nodes that converts markdown headings to ADF HeadingNode.
 */
class HeadingTranscriber(
    private val mapper: MarkdownNodeMapper,
) : MarkdownTranscriber<HeadingNode> {
    override fun transcribe(
        input: ASTNode,
        context: MarkdownContext,
    ): TranscribeResult<HeadingNode> {
        val level = headingLevel(input)

        // Find ATX_CONTENT or SETEXT_CONTENT, or process children for content
        val atxContentNode = input.findChildOfType(MarkdownTokenTypes.ATX_CONTENT)
        val setextContentNode = input.findChildOfType(MarkdownTokenTypes.SETEXT_CONTENT)
        val contentNode = atxContentNode ?: setextContentNode
        val inlineResult =
            if (contentNode != null) {
                // Skip the first child if it's whitespace (space after # in ATX headings)
                val childrenToProcess =
                    contentNode.children.let { children ->
                        if (children.firstOrNull()?.type == MarkdownTokenTypes.WHITE_SPACE) {
                            children.drop(1)
                        } else {
                            children
                        }
                    }
                mapper.transcribeInlineChildren(childrenToProcess, context)
            } else {
                // If no content node found, process all children that are inline
                mapper.transcribeInlineChildren(input, context)
            }

        return TranscribeResult(
            HeadingNode(
                attrs = HeadingAttrs(level = level),
                content = inlineResult.content,
            ),
            inlineResult.actions,
        )
    }

    private fun headingLevel(input: ASTNode): Int =
        when (input.type) {
            MarkdownElementTypes.ATX_1 -> 1
            MarkdownElementTypes.ATX_2 -> 2
            MarkdownElementTypes.ATX_3 -> 3
            MarkdownElementTypes.ATX_4 -> 4
            MarkdownElementTypes.ATX_5 -> 5
            MarkdownElementTypes.ATX_6 -> 6
            MarkdownElementTypes.SETEXT_1 -> 1
            MarkdownElementTypes.SETEXT_2 -> 2
            else -> 1
        }
}

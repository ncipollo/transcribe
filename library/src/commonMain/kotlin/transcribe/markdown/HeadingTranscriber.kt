package transcribe.markdown

import data.atlassian.adf.HeadingNode
import data.atlassian.adf.HeadingAttrs
import org.intellij.markdown.MarkdownElementTypes
import org.intellij.markdown.MarkdownTokenTypes
import org.intellij.markdown.ast.ASTNode
import org.intellij.markdown.ast.findChildOfType
import transcribe.TranscribeResult

/**
 * Transcriber for ATX_1-6 and SETEXT_1-2 heading nodes that converts markdown headings to ADF HeadingNode.
 */
class HeadingTranscriber(
    private val inlineTranscriber: InlineContentTranscriber,
) : MarkdownTranscriber<HeadingNode> {
    override fun transcribe(
        input: ASTNode,
        context: MarkdownContext,
    ): TranscribeResult<HeadingNode> {
        val level = when (input.type) {
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

        // Find ATX_CONTENT or process children for content
        val contentNode = input.findChildOfType(MarkdownTokenTypes.ATX_CONTENT)
        val inlineContent = if (contentNode != null) {
            inlineTranscriber.transcribeChildren(contentNode, context)
        } else {
            // For SETEXT or if no ATX_CONTENT, process all children that are inline
            inlineTranscriber.transcribeChildren(input, context)
        }

        return TranscribeResult(
            HeadingNode(
                attrs = HeadingAttrs(level = level),
                content = inlineContent,
            ),
        )
    }
}


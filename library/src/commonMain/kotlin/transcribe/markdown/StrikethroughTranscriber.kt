package transcribe.markdown

import data.atlassian.adf.StrikeMark
import data.atlassian.adf.TextNode
import org.intellij.markdown.ast.ASTNode
import transcribe.TranscribeResult

/**
 * Transcriber for STRIKETHROUGH nodes (GFM) that converts markdown strikethrough to ADF TextNode with StrikeMark.
 */
class StrikethroughTranscriber(
    private val inlineTranscriber: InlineContentTranscriber,
) : MarkdownTranscriber<TextNode> {
    override fun transcribe(
        input: ASTNode,
        context: MarkdownContext,
    ): TranscribeResult<TextNode> {
        // Transcribe all inline children and combine their text
        val inlineNodes = inlineTranscriber.transcribeChildren(input, context)
        val text =
            inlineNodes.joinToString("") { node ->
                if (node is TextNode) {
                    node.text
                } else {
                    ""
                }
            }

        return TranscribeResult(
            TextNode(
                text = text,
                marks = listOf(StrikeMark),
            ),
        )
    }
}

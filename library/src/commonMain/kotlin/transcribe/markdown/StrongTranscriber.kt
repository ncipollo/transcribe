package transcribe.markdown

import data.atlassian.adf.StrongMark
import data.atlassian.adf.TextNode
import org.intellij.markdown.ast.ASTNode
import transcribe.TranscribeResult

/**
 * Transcriber for STRONG nodes that converts markdown strong (bold) to ADF TextNode with StrongMark.
 */
class StrongTranscriber(
    private val mapper: MarkdownNodeMapper,
) : MarkdownTranscriber<TextNode> {
    override fun transcribe(
        input: ASTNode,
        context: MarkdownContext,
    ): TranscribeResult<TextNode> {
        // Transcribe all inline children and combine their text
        val inlineNodes = mapper.transcribeInlineChildren(input, context)
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
                marks = listOf(StrongMark),
            ),
        )
    }
}

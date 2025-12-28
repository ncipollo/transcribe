package transcribe.markdown

import data.atlassian.adf.EmMark
import data.atlassian.adf.TextNode
import org.intellij.markdown.ast.ASTNode
import transcribe.TranscribeResult

/**
 * Transcriber for EMPH nodes that converts markdown emphasis (italic) to ADF TextNode with EmMark.
 */
class EmphasisTranscriber(
    private val inlineTranscriber: InlineContentTranscriber,
) : MarkdownTranscriber<TextNode> {
    override fun transcribe(
        input: ASTNode,
        context: MarkdownContext,
    ): TranscribeResult<TextNode> {
        // Transcribe all inline children and combine their text
        val inlineNodes = inlineTranscriber.transcribeChildren(input, context)
        val text = inlineNodes.joinToString("") { node ->
            if (node is TextNode) {
                node.text
            } else {
                ""
            }
        }

        return TranscribeResult(
            TextNode(
                text = text,
                marks = listOf(EmMark),
            ),
        )
    }
}


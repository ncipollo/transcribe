package transcribe.markdown

import data.atlassian.adf.EmMark
import data.atlassian.adf.TextNode
import data.markdown.parser.findTextContent
import org.intellij.markdown.ast.ASTNode
import transcribe.TranscribeResult

/**
 * Transcriber for EMPH nodes that converts markdown emphasis (italic) to ADF TextNode with EmMark.
 */
class EmphasisTranscriber : MarkdownTranscriber<TextNode> {
    override fun transcribe(
        input: ASTNode,
        context: MarkdownContext,
    ): TranscribeResult<TextNode> {
        val text = input.findTextContent(context.markdownText)
        return TranscribeResult(
            TextNode(
                text = text,
                marks = listOf(EmMark),
            ),
        )
    }
}

package transcribe.markdown

import context.MarkdownContext
import data.atlassian.adf.StrongMark
import data.atlassian.adf.TextNode
import data.markdown.parser.findTextContent
import org.intellij.markdown.ast.ASTNode
import transcribe.TranscribeResult

/**
 * Transcriber for STRONG nodes that converts markdown strong (bold) to ADF TextNode with StrongMark.
 */
class StrongTranscriber : MarkdownTranscriber<TextNode> {
    override fun transcribe(
        input: ASTNode,
        context: MarkdownContext,
    ): TranscribeResult<TextNode> {
        val text = input.findTextContent(context.markdownText)
        return TranscribeResult(
            TextNode(
                text = text,
                marks = listOf(StrongMark),
            ),
        )
    }
}

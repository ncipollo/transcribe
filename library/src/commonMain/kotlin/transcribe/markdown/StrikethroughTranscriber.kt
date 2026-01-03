package transcribe.markdown

import context.MarkdownContext
import data.atlassian.adf.StrikeMark
import data.atlassian.adf.TextNode
import data.markdown.parser.findTextContent
import org.intellij.markdown.ast.ASTNode
import transcribe.TranscribeResult

/**
 * Transcriber for STRIKETHROUGH nodes (GFM) that converts markdown strikethrough to ADF TextNode with StrikeMark.
 */
class StrikethroughTranscriber : MarkdownTranscriber<TextNode> {
    override fun transcribe(
        input: ASTNode,
        context: MarkdownContext,
    ): TranscribeResult<TextNode> {
        val text = input.findTextContent(context.markdownText)
        return TranscribeResult(
            TextNode(
                text = text,
                marks = listOf(StrikeMark),
            ),
        )
    }
}

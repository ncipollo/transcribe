package transcribe.markdown

import context.MarkdownContext
import data.atlassian.adf.TextNode
import data.markdown.parser.getTextContent
import org.intellij.markdown.ast.ASTNode
import transcribe.TranscribeResult

/**
 * Transcriber for WHITE_SPACE nodes that converts markdown whitespace to ADF TextNode.
 */
class WhitespaceTranscriber : MarkdownTranscriber<TextNode> {
    override fun transcribe(
        input: ASTNode,
        context: MarkdownContext,
    ): TranscribeResult<TextNode> {
        val text = input.getTextContent(context.markdownText).toString()
        return TranscribeResult(TextNode(text = text))
    }
}

package transcribe.markdown

import data.atlassian.adf.TextNode
import data.markdown.parser.getTextContent
import org.intellij.markdown.MarkdownElementTypes
import org.intellij.markdown.ast.ASTNode
import transcribe.TranscribeResult

/**
 * Transcriber for TEXT nodes that converts markdown text to ADF TextNode.
 */
class TextTranscriber : MarkdownTranscriber<TextNode> {
    override fun transcribe(
        input: ASTNode,
        context: MarkdownContext,
    ): TranscribeResult<TextNode> {
        val text = input.getTextContent(context.markdownText).toString()
        return TranscribeResult(TextNode(text = text))
    }
}


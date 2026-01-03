package transcribe.markdown

import context.MarkdownContext
import data.atlassian.adf.CodeMark
import data.atlassian.adf.TextNode
import data.markdown.parser.getTextContent
import org.intellij.markdown.ast.ASTNode
import transcribe.TranscribeResult

/**
 * Transcriber for CODE_SPAN nodes that converts markdown inline code to ADF TextNode with CodeMark.
 */
class CodeSpanTranscriber : MarkdownTranscriber<TextNode> {
    override fun transcribe(
        input: ASTNode,
        context: MarkdownContext,
    ): TranscribeResult<TextNode> {
        // Extract text content, excluding the backticks
        val text =
            input.getTextContent(context.markdownText).toString()
                .removePrefix("`")
                .removeSuffix("`")

        return TranscribeResult(
            TextNode(
                text = text,
                marks = listOf(CodeMark),
            ),
        )
    }
}

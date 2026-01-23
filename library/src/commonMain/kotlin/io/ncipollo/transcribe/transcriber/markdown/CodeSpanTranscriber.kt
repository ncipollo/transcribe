package io.ncipollo.transcribe.transcriber.markdown

import io.ncipollo.transcribe.context.MarkdownContext
import io.ncipollo.transcribe.data.atlassian.adf.CodeMark
import io.ncipollo.transcribe.data.atlassian.adf.TextNode
import io.ncipollo.transcribe.data.markdown.parser.getTextContent
import org.intellij.markdown.ast.ASTNode
import io.ncipollo.transcribe.transcriber.TranscribeResult

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

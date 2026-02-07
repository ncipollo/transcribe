package io.ncipollo.transcribe.transcriber.markdown

import io.ncipollo.transcribe.context.MarkdownContext
import io.ncipollo.transcribe.data.atlassian.adf.StrongMark
import io.ncipollo.transcribe.data.atlassian.adf.TextNode
import io.ncipollo.transcribe.data.markdown.parser.getTextContentWithoutDelimiters
import org.intellij.markdown.ast.ASTNode
import io.ncipollo.transcribe.transcriber.TranscribeResult

/**
 * Transcriber for STRONG nodes that converts markdown strong (bold) to ADF TextNode with StrongMark.
 */
class StrongTranscriber : MarkdownTranscriber<TextNode> {
    override fun transcribe(
        input: ASTNode,
        context: MarkdownContext,
    ): TranscribeResult<TextNode> {
        val text = input.getTextContentWithoutDelimiters(context.markdownText, "**")
        return TranscribeResult(
            TextNode(
                text = text,
                marks = listOf(StrongMark),
            ),
        )
    }
}

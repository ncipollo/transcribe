package io.ncipollo.transcribe.transcriber.markdown

import io.ncipollo.transcribe.context.MarkdownContext
import io.ncipollo.transcribe.data.atlassian.adf.StrikeMark
import io.ncipollo.transcribe.data.atlassian.adf.TextNode
import io.ncipollo.transcribe.data.markdown.parser.getTextContentWithoutDelimiters
import org.intellij.markdown.ast.ASTNode
import io.ncipollo.transcribe.transcriber.TranscribeResult

/**
 * Transcriber for STRIKETHROUGH nodes (GFM) that converts markdown strikethrough to ADF TextNode with StrikeMark.
 */
class StrikethroughTranscriber : MarkdownTranscriber<TextNode> {
    override fun transcribe(
        input: ASTNode,
        context: MarkdownContext,
    ): TranscribeResult<TextNode> {
        val text = input.getTextContentWithoutDelimiters(context.markdownText, "~~")
        return TranscribeResult(
            TextNode(
                text = text,
                marks = listOf(StrikeMark),
            ),
        )
    }
}

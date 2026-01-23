package io.ncipollo.transcribe.transcriber.markdown

import io.ncipollo.transcribe.context.MarkdownContext
import io.ncipollo.transcribe.data.atlassian.adf.HardBreakNode
import org.intellij.markdown.ast.ASTNode
import io.ncipollo.transcribe.transcriber.TranscribeResult

/**
 * Transcriber for EOL (end-of-line) nodes that converts markdown line breaks to ADF HardBreakNode.
 */
class EolTranscriber : MarkdownTranscriber<HardBreakNode> {
    override fun transcribe(
        input: ASTNode,
        context: MarkdownContext,
    ): TranscribeResult<HardBreakNode> {
        return TranscribeResult(HardBreakNode())
    }
}

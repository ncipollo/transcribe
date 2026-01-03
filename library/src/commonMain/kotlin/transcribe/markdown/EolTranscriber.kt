package transcribe.markdown

import context.MarkdownContext
import data.atlassian.adf.HardBreakNode
import org.intellij.markdown.ast.ASTNode
import transcribe.TranscribeResult

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

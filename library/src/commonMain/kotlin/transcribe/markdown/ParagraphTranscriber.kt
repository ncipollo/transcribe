package transcribe.markdown

import context.MarkdownContext
import data.atlassian.adf.ParagraphNode
import org.intellij.markdown.ast.ASTNode
import transcribe.TranscribeResult

/**
 * Transcriber for PARAGRAPH nodes that converts markdown paragraphs to ADF ParagraphNode.
 */
class ParagraphTranscriber(
    private val mapper: MarkdownNodeMapper,
) : MarkdownTranscriber<ParagraphNode> {
    override fun transcribe(
        input: ASTNode,
        context: MarkdownContext,
    ): TranscribeResult<ParagraphNode> {
        val inlineContent = mapper.transcribeInlineChildren(input, context)

        return TranscribeResult(
            ParagraphNode(
                content = inlineContent,
            ),
        )
    }
}

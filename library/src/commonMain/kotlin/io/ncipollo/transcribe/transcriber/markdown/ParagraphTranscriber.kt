package io.ncipollo.transcribe.transcriber.markdown

import io.ncipollo.transcribe.context.MarkdownContext
import io.ncipollo.transcribe.data.atlassian.adf.ParagraphNode
import org.intellij.markdown.ast.ASTNode
import io.ncipollo.transcribe.transcriber.TranscribeResult

/**
 * Transcriber for PARAGRAPH nodes that converts markdown paragraphs to ADF ParagraphNode.
 */
class ParagraphTranscriber(
    private val mapper: MarkdownNodeMapper,
) : MarkdownTranscriber<ParagraphNode> {
    private val markAccumulator = InlineMarkAccumulator(mapper)

    override fun transcribe(
        input: ASTNode,
        context: MarkdownContext,
    ): TranscribeResult<ParagraphNode> {
        val inlineResult = markAccumulator.transcribeWithMarks(input, context)

        return TranscribeResult(
            ParagraphNode(
                content = inlineResult.content,
            ),
            inlineResult.actions,
        )
    }
}

package io.ncipollo.transcribe.transcriber.markdown

import io.ncipollo.transcribe.context.MarkdownContext
import io.ncipollo.transcribe.data.atlassian.adf.BlockquoteNode
import org.intellij.markdown.ast.ASTNode
import io.ncipollo.transcribe.transcriber.TranscribeResult

/**
 * Transcriber for BLOCK_QUOTE nodes that converts markdown blockquotes to ADF BlockquoteNode.
 */
class BlockquoteTranscriber(
    private val nodeMapper: MarkdownNodeMapper,
) : MarkdownTranscriber<BlockquoteNode> {
    override fun transcribe(
        input: ASTNode,
        context: MarkdownContext,
    ): TranscribeResult<BlockquoteNode> {
        val blockResult = nodeMapper.transcribeBlockChildren(input, context)
        val cleanedContent = BlockquoteCleanupHelper.cleanupBlockContent(blockResult.content)
        return TranscribeResult(
            BlockquoteNode(
                content = cleanedContent,
            ),
            blockResult.actions,
        )
    }
}

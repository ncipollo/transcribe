package transcribe.markdown

import data.atlassian.adf.BlockquoteNode
import org.intellij.markdown.ast.ASTNode
import transcribe.TranscribeResult

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
        val blockContent = nodeMapper.transcribeBlockChildren(input, context)
        val cleanedContent = BlockquoteCleanupHelper.cleanupBlockContent(blockContent)
        return TranscribeResult(
            BlockquoteNode(
                content = cleanedContent,
            ),
        )
    }
}

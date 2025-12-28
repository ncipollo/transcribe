package transcribe.markdown

import data.atlassian.adf.ADFBlockNode
import data.atlassian.adf.BlockquoteNode
import org.intellij.markdown.MarkdownElementTypes
import org.intellij.markdown.ast.ASTNode
import transcribe.TranscribeResult

/**
 * Transcriber for BLOCK_QUOTE nodes that converts markdown blockquotes to ADF BlockquoteNode.
 */
class BlockquoteTranscriber(
    private val blockTranscriber: BlockContentTranscriber,
) : MarkdownTranscriber<BlockquoteNode> {
    override fun transcribe(
        input: ASTNode,
        context: MarkdownContext,
    ): TranscribeResult<BlockquoteNode> {
        // Blockquotes can contain paragraphs, lists, etc.
        // Process all block-level children
        val blockContent = blockTranscriber.transcribeChildren(input, context)

        return TranscribeResult(
            BlockquoteNode(
                content = blockContent,
            ),
        )
    }
}


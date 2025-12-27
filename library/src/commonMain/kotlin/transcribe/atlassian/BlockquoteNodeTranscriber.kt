package transcribe.atlassian

import data.atlassian.adf.BlockquoteNode
import transcribe.TranscribeResult

/**
 * Transcriber for BlockquoteNode that converts ADF blockquote to markdown blockquote.
 * Outputs > prefixed lines for each block child.
 */
class BlockquoteNodeTranscriber(
    private val mapper: ADFNodeMapper,
) : ADFTranscriber<BlockquoteNode> {
    override fun transcribe(
        input: BlockquoteNode,
        context: ADFTranscriberContext,
    ): TranscribeResult<String> {
        val content = input.content
        if (content.isEmpty()) {
            return TranscribeResult("")
        }

        val nodeTranscriber = ADFNodeTranscriber(mapper)
        val markdown =
            content.joinToString("\n") { block ->
                val blockContent = nodeTranscriber.transcribe(block, context).content
                // Prefix each line with >
                blockContent.trimEnd('\n').lines().joinToString("\n") { line ->
                    if (line.isBlank()) {
                        ">"
                    } else {
                        "> $line"
                    }
                }
            }

        return TranscribeResult(markdown)
    }
}

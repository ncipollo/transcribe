package io.ncipollo.transcribe.transcriber.atlassian

import io.ncipollo.transcribe.context.ADFTranscriberContext
import io.ncipollo.transcribe.data.atlassian.adf.BlockquoteNode
import io.ncipollo.transcribe.transcriber.TranscribeResult

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
        val results = content.map { block -> nodeTranscriber.transcribe(block, context) }
        val markdown = results.joinToString("\n") { result ->
            val blockContent = result.content
            // Prefix each line with >
            blockContent.trimEnd('\n').lines().joinToString("\n") { line ->
                if (line.isBlank()) {
                    ">"
                } else {
                    "> $line"
                }
            }
        }
        val actions = results.flatMap { it.actions }
        return TranscribeResult(markdown, actions)
    }
}

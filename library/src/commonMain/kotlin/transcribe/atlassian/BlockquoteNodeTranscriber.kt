package transcribe.atlassian

import data.atlassian.adf.BlockquoteNode
import transcribe.TranscribeResult

/**
 * Transcriber for BlockquoteNode that converts ADF blockquote to markdown blockquote.
 * Outputs > prefixed lines for each block child.
 */
class BlockquoteNodeTranscriber : ADFTranscriber<BlockquoteNode> {
    override fun transcribe(input: BlockquoteNode): TranscribeResult<String> {
        val content = input.content
        if (content.isEmpty()) {
            return TranscribeResult("")
        }
        
        val markdown = content.joinToString("") { block ->
            val blockContent = ADFNodeTranscriber.transcribeBlock(block).content
            // Prefix each line with >
            blockContent.trimEnd('\n').lines().joinToString("\n") { line ->
                if (line.isBlank()) {
                    ">"
                } else {
                    "> $line"
                }
            } + "\n"
        }
        
        return TranscribeResult("$markdown\n")
    }
}


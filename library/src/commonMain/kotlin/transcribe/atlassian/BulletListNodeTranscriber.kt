package transcribe.atlassian

import data.atlassian.adf.BulletListNode
import transcribe.TranscribeResult

/**
 * Transcriber for BulletListNode that converts ADF bullet list to markdown.
 * Outputs - prefixed items, one per line.
 */
class BulletListNodeTranscriber(
    private val mapper: ADFNodeMapper
) : ADFTranscriber<BulletListNode> {
    override fun transcribe(input: BulletListNode): TranscribeResult<String> {
        val content = input.content
        if (content.isEmpty()) {
            return TranscribeResult("")
        }
        
        val nodeTranscriber = ADFNodeTranscriber(mapper)
        val markdown = content.joinToString("\n") { item ->
            val itemContent = nodeTranscriber.transcribe(item).content
            "- $itemContent"
        }
        
        return TranscribeResult("$markdown\n\n")
    }
}


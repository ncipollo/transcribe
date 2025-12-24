package transcribe.atlassian

import data.atlassian.adf.ADFNode
import data.atlassian.adf.BulletListNode
import kotlin.reflect.KClass
import transcribe.TranscribeResult

/**
 * Transcriber for BulletListNode that converts ADF bullet list to markdown.
 * Outputs - prefixed items, one per line.
 */
class BulletListNodeTranscriber(
    private val nodeMap: Map<KClass<out ADFNode>, ADFTranscriber<*>>
) : ADFTranscriber<BulletListNode> {
    override fun transcribe(input: BulletListNode): TranscribeResult<String> {
        val content = input.content
        if (content.isEmpty()) {
            return TranscribeResult("")
        }
        
        val nodeTranscriber = ADFNodeTranscriber(nodeMap)
        val markdown = content.joinToString("\n") { item ->
            val itemContent = nodeTranscriber.transcribe(item).content
            "- $itemContent"
        }
        
        return TranscribeResult("$markdown\n\n")
    }
}


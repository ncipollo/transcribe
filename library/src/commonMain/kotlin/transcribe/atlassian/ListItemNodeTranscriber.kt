package transcribe.atlassian

import data.atlassian.adf.ADFNode
import data.atlassian.adf.ListItemNode
import kotlin.reflect.KClass
import transcribe.TranscribeResult

/**
 * Transcriber for ListItemNode that converts ADF list item to markdown.
 * Handles item content and nesting. The prefix (- or number) is added by parent list.
 */
class ListItemNodeTranscriber(
    private val nodeMap: Map<KClass<out ADFNode>, ADFTranscriber<*>>
) : ADFTranscriber<ListItemNode> {
    override fun transcribe(input: ListItemNode): TranscribeResult<String> {
        val content = input.content
        if (content.isEmpty()) {
            return TranscribeResult("")
        }
        
        val nodeTranscriber = ADFNodeTranscriber(nodeMap)
        val markdown = content.joinToString("") { node ->
            nodeTranscriber.transcribe(node).content
        }
        
        return TranscribeResult(markdown)
    }
}


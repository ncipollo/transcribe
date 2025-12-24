package transcribe.atlassian

import data.atlassian.adf.ADFNode
import data.atlassian.adf.DocNode
import kotlin.reflect.KClass
import transcribe.TranscribeResult

class ADFDocumentTranscriber(
    private val nodeMap: Map<KClass<out ADFNode>, ADFTranscriber<*>>
) : ADFTranscriber<DocNode> {
    override fun transcribe(input: DocNode): TranscribeResult<String> {
        val nodeTranscriber = ADFNodeTranscriber(nodeMap)
        val markdown = input.content.joinToString("") { node ->
            nodeTranscriber.transcribe(node).content
        }
        return TranscribeResult(markdown)
    }
}


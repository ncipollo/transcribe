package transcribe.atlassian

import data.atlassian.adf.ADFNode
import data.atlassian.adf.ParagraphNode
import kotlin.reflect.KClass
import transcribe.TranscribeResult

/**
 * Transcriber for ParagraphNode that converts ADF paragraph to markdown.
 * Transcribes inline content and appends newline.
 */
class ParagraphNodeTranscriber(
    private val nodeMap: Map<KClass<out ADFNode>, ADFTranscriber<*>>
) : ADFTranscriber<ParagraphNode> {
    override fun transcribe(input: ParagraphNode): TranscribeResult<String> {
        val content = input.content
        val markdown = if (content.isNullOrEmpty()) {
            ""
        } else {
            val nodeTranscriber = ADFNodeTranscriber(nodeMap)
            content.joinToString("") { node ->
                nodeTranscriber.transcribe(node).content
            }
        }
        return TranscribeResult("$markdown\n")
    }
}


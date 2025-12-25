package transcribe.atlassian

import data.atlassian.adf.ParagraphNode
import transcribe.TranscribeResult

/**
 * Transcriber for ParagraphNode that converts ADF paragraph to markdown.
 * Transcribes inline content and appends newline.
 */
class ParagraphNodeTranscriber(
    private val mapper: ADFNodeMapper,
) : ADFTranscriber<ParagraphNode> {
    override fun transcribe(input: ParagraphNode): TranscribeResult<String> {
        val content = input.content
        val markdown =
            if (content.isNullOrEmpty()) {
                ""
            } else {
                val nodeTranscriber = ADFNodeTranscriber(mapper)
                content.joinToString("") { node ->
                    nodeTranscriber.transcribe(node).content
                }
            }
        return TranscribeResult("$markdown\n")
    }
}

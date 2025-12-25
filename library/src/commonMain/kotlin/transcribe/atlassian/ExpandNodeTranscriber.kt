package transcribe.atlassian

import data.atlassian.adf.ExpandNode
import transcribe.TranscribeResult

/**
 * Transcriber for ExpandNode that converts ADF expand to markdown HTML details element.
 * Outputs <details> and <summary> tags with the title from attrs or default "Click to expand".
 */
class ExpandNodeTranscriber(
    private val mapper: ADFNodeMapper,
) : ADFTranscriber<ExpandNode> {
    override fun transcribe(input: ExpandNode, context: ADFTranscriberContext): TranscribeResult<String> {
        val content = input.content
        val summaryText = input.attrs?.title ?: "Click to expand"

        if (content.isEmpty()) {
            return TranscribeResult("<details>\n<summary>$summaryText</summary>\n</details>\n")
        }

        val nodeTranscriber = ADFNodeTranscriber(mapper)
        val markdown =
            content.joinToString("") { block ->
                nodeTranscriber.transcribe(block, context).content
            }

        return TranscribeResult("<details>\n<summary>$summaryText</summary>\n$markdown</details>\n")
    }
}


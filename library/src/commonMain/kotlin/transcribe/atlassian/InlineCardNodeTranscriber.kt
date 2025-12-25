package transcribe.atlassian

import data.atlassian.adf.InlineCardNode
import transcribe.TranscribeResult

/**
 * Transcriber for InlineCardNode that converts ADF inline card to markdown link.
 * Outputs [url](url) format if URL is available, otherwise empty string.
 */
class InlineCardNodeTranscriber : ADFTranscriber<InlineCardNode> {
    override fun transcribe(input: InlineCardNode, context: ADFTranscriberContext): TranscribeResult<String> {
        val url = input.attrs.url
        return if (url != null && url.isNotBlank()) {
            TranscribeResult("[$url]($url)")
        } else {
            TranscribeResult("")
        }
    }
}

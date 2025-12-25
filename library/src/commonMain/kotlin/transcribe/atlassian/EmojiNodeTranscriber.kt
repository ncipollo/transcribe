package transcribe.atlassian

import data.atlassian.adf.EmojiNode
import transcribe.TranscribeResult

/**
 * Transcriber for EmojiNode that converts ADF emoji to markdown emoji syntax.
 * Outputs :shortname: format, or falls back to text if available.
 */
class EmojiNodeTranscriber : ADFTranscriber<EmojiNode> {
    override fun transcribe(input: EmojiNode, context: ADFTranscriberContext): TranscribeResult<String> {
        val text = input.attrs.text
        return if (text != null && text.isNotBlank()) {
            TranscribeResult(text)
        } else {
            TranscribeResult(":${input.attrs.shortName}:")
        }
    }
}

package transcribe.atlassian

import data.atlassian.adf.StatusColor
import data.atlassian.adf.StatusNode
import transcribe.TranscribeResult

/**
 * Transcriber for StatusNode that converts ADF status badge to markdown with emoji prefix.
 * Outputs [{emoji} {text}] format based on status color.
 */
class StatusNodeTranscriber : ADFTranscriber<StatusNode> {
    override fun transcribe(input: StatusNode, context: ADFTranscriberContext): TranscribeResult<String> {
        val emoji = when (input.attrs.color) {
            StatusColor.BLUE -> "🔵"
            StatusColor.GREEN -> "🟢"
            StatusColor.RED -> "🔴"
            StatusColor.YELLOW -> "🟡"
            StatusColor.PURPLE -> "🟣"
            StatusColor.NEUTRAL -> "⚪"
        }
        return TranscribeResult("[$emoji ${input.attrs.text}]")
    }
}


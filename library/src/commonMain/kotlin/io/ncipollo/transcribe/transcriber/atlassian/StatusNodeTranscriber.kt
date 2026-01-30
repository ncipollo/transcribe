package io.ncipollo.transcribe.transcriber.atlassian

import io.ncipollo.transcribe.context.ADFTranscriberContext
import io.ncipollo.transcribe.data.atlassian.adf.StatusColor
import io.ncipollo.transcribe.data.atlassian.adf.StatusNode
import io.ncipollo.transcribe.transcriber.TranscribeResult

/**
 * Transcriber for StatusNode that converts ADF status badge to markdown with emoji prefix.
 * Outputs {emoji} {text} format based on status color.
 */
class StatusNodeTranscriber : ADFTranscriber<StatusNode> {
    override fun transcribe(
        input: StatusNode,
        context: ADFTranscriberContext,
    ): TranscribeResult<String> {
        val emoji =
            when (input.attrs.color) {
                StatusColor.BLUE -> "🔵"
                StatusColor.GREEN -> "🟢"
                StatusColor.RED -> "🔴"
                StatusColor.YELLOW -> "🟡"
                StatusColor.PURPLE -> "🟣"
                StatusColor.NEUTRAL -> "⚪"
            }
        return TranscribeResult("$emoji ${input.attrs.text.uppercase()}")
    }
}

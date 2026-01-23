package io.ncipollo.transcribe.transcriber.atlassian

import io.ncipollo.transcribe.data.atlassian.adf.ADFMark
import io.ncipollo.transcribe.data.atlassian.adf.BackgroundColorMark
import io.ncipollo.transcribe.data.atlassian.adf.CodeMark
import io.ncipollo.transcribe.data.atlassian.adf.EmMark
import io.ncipollo.transcribe.data.atlassian.adf.LinkMark
import io.ncipollo.transcribe.data.atlassian.adf.StrikeMark
import io.ncipollo.transcribe.data.atlassian.adf.StrongMark
import io.ncipollo.transcribe.data.atlassian.adf.SubSupMark
import io.ncipollo.transcribe.data.atlassian.adf.SubSupType
import io.ncipollo.transcribe.data.atlassian.adf.TextColorMark
import io.ncipollo.transcribe.data.atlassian.adf.UnderlineMark

/**
 * Helper object for applying ADF marks to text.
 *
 * Mark formatting:
 * - StrongMark: **text**
 * - EmMark: *text*
 * - CodeMark: `text`
 * - StrikeMark: ~~text~~
 * - UnderlineMark: <u>text</u>
 * - LinkMark: [text](href)
 * - TextColorMark: <span style="color: #hex">text</span>
 * - BackgroundColorMark: <span style="background-color: #hex">text</span>
 * - SubSupMark: <sub>text</sub> or <sup>text</sup>
 *
 * Other marks (alignment, indentation, annotation, border, breakout, dataConsumer, fragment, unknown)
 * are ignored as they don't have text-level equivalents.
 */
object MarkApplicator {
    /**
     * Applies a list of marks to the given text, returning formatted output.
     */
    fun applyMarks(
        text: String,
        marks: List<ADFMark>?,
    ): String {
        if (marks.isNullOrEmpty()) return text

        return marks.fold(text) { acc, mark -> applyMark(acc, mark) }
    }

    private fun applyMark(
        text: String,
        mark: ADFMark,
    ): String {
        val leading = text.takeWhile { it.isWhitespace() }
        val trailing = text.takeLastWhile { it.isWhitespace() }
        val trimmed = text.trim()

        val formatted =
            when (mark) {
                is StrongMark -> "**$trimmed**"
                is EmMark -> "*$trimmed*"
                is CodeMark -> "`$trimmed`"
                is StrikeMark -> "~~$trimmed~~"
                is UnderlineMark -> "<u>$trimmed</u>"
                is LinkMark -> "[$trimmed](${mark.attrs.href})"
                is TextColorMark -> "<span style=\"color: ${mark.attrs.color}\">$trimmed</span>"
                is BackgroundColorMark -> "<span style=\"background-color: ${mark.attrs.color}\">$trimmed</span>"
                is SubSupMark ->
                    when (mark.attrs.type) {
                        SubSupType.SUB -> "<sub>$trimmed</sub>"
                        SubSupType.SUP -> "<sup>$trimmed</sup>"
                    }
                else -> return text
            }

        return "$leading$formatted$trailing"
    }
}

package transcribe.atlassian

import data.atlassian.adf.ADFMark
import data.atlassian.adf.BackgroundColorMark
import data.atlassian.adf.CodeMark
import data.atlassian.adf.EmMark
import data.atlassian.adf.LinkMark
import data.atlassian.adf.StrikeMark
import data.atlassian.adf.StrongMark
import data.atlassian.adf.SubSupMark
import data.atlassian.adf.SubSupType
import data.atlassian.adf.TextColorMark
import data.atlassian.adf.UnderlineMark

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
    fun applyMarks(text: String, marks: List<ADFMark>?): String {
        if (marks.isNullOrEmpty()) return text

        return marks.fold(text) { acc, mark -> applyMark(acc, mark) }
    }

    private fun applyMark(text: String, mark: ADFMark): String {
        return when (mark) {
            is StrongMark -> "**$text**"
            is EmMark -> "*$text*"
            is CodeMark -> "`$text`"
            is StrikeMark -> "~~$text~~"
            is UnderlineMark -> "<u>$text</u>"
            is LinkMark -> "[$text](${mark.attrs.href})"
            is TextColorMark -> "<span style=\"color: ${mark.attrs.color}\">$text</span>"
            is BackgroundColorMark -> "<span style=\"background-color: ${mark.attrs.color}\">$text</span>"
            is SubSupMark -> when (mark.attrs.type) {
                SubSupType.SUB -> "<sub>$text</sub>"
                SubSupType.SUP -> "<sup>$text</sup>"
            }
            else -> text
        }
    }
}


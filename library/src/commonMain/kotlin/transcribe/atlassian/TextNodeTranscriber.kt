package transcribe.atlassian

import data.atlassian.adf.TextNode
import transcribe.TranscribeResult

/**
 * Transcriber for TextNode that converts ADF text with marks to markdown/HTML string.
 */
class TextNodeTranscriber : ADFTranscriber<TextNode> {
    override fun transcribe(
        input: TextNode,
        context: ADFTranscriberContext,
    ): TranscribeResult<String> {
        val markedText = MarkApplicator.applyMarks(input.text, input.marks)
        return TranscribeResult(markedText)
    }
}

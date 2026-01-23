package io.ncipollo.transcribe.transcriber.atlassian

import io.ncipollo.transcribe.context.ADFTranscriberContext
import io.ncipollo.transcribe.data.atlassian.adf.TextNode
import io.ncipollo.transcribe.transcriber.TranscribeResult

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

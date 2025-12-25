package transcribe.atlassian

import data.atlassian.adf.ExpandAttrs
import data.atlassian.adf.ExpandNode
import data.atlassian.adf.NestedExpandNode
import transcribe.TranscribeResult

/**
 * Transcriber for NestedExpandNode that converts ADF nested expand to markdown HTML details element.
 * Delegates to ExpandNodeTranscriber since the output format is identical.
 */
class NestedExpandNodeTranscriber(
    private val mapper: ADFNodeMapper,
) : ADFTranscriber<NestedExpandNode> {
    private val expandTranscriber = ExpandNodeTranscriber(mapper)

    override fun transcribe(input: NestedExpandNode): TranscribeResult<String> {
        // Convert NestedExpandNode to ExpandNode and delegate
        val expandNode =
            ExpandNode(
                content = input.content,
                attrs = ExpandAttrs(title = input.attrs.title, localId = input.attrs.localId),
            )
        return expandTranscriber.transcribe(expandNode)
    }
}


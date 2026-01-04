package transcribe.atlassian

import context.ADFTranscriberContext
import data.atlassian.adf.HeadingNode
import transcribe.TranscribeResult

/**
 * Transcriber for HeadingNode that converts ADF heading to markdown heading.
 * Outputs # prefix based on level (1-6), followed by content and double newline.
 */
class HeadingNodeTranscriber(
    private val mapper: ADFNodeMapper,
) : ADFTranscriber<HeadingNode> {
    override fun transcribe(
        input: HeadingNode,
        context: ADFTranscriberContext,
    ): TranscribeResult<String> {
        val level = input.attrs.level.coerceIn(1, 6)
        val prefix = "#".repeat(level)

        val content = input.content
        val markdown: String
        val actions: List<transcribe.action.TranscriberAction>
        if (content.isNullOrEmpty()) {
            markdown = ""
            actions = emptyList()
        } else {
            val nodeTranscriber = ADFNodeTranscriber(mapper)
            val results = content.map { node -> nodeTranscriber.transcribe(node, context) }
            markdown = results.joinToString("") { it.content }
            actions = results.flatMap { it.actions }
        }

        return TranscribeResult("\n$prefix $markdown\n", actions)
    }
}

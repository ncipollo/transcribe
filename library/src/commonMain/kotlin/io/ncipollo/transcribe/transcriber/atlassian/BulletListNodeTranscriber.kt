package io.ncipollo.transcribe.transcriber.atlassian

import io.ncipollo.transcribe.context.ADFTranscriberContext
import io.ncipollo.transcribe.data.atlassian.adf.BulletListNode
import io.ncipollo.transcribe.transcriber.TranscribeResult

/**
 * Transcriber for BulletListNode that converts ADF bullet list to markdown.
 * Outputs - prefixed items, one per line.
 */
class BulletListNodeTranscriber(
    private val mapper: ADFNodeMapper,
) : ADFTranscriber<BulletListNode> {
    override fun transcribe(
        input: BulletListNode,
        context: ADFTranscriberContext,
    ): TranscribeResult<String> {
        val content = input.content
        if (content.isEmpty()) {
            return TranscribeResult("")
        }

        val nodeTranscriber = ADFNodeTranscriber(mapper)
        val results = content.map { item -> nodeTranscriber.transcribe(item, context) }
        val indent = "    ".repeat(context.listLevel)
        val markdown = results.joinToString("") { result ->
            val lines = result.content.trimEnd('\n').lines()
            lines.mapIndexed { index, line ->
                if (index == 0) {
                    // First line: add indentation and bullet marker
                    "$indent- $line\n"
                } else {
                    // Continuation lines: already have proper indentation from nested lists
                    "$line\n"
                }
            }.joinToString("")
        }
        val actions = results.flatMap { it.actions }
        return TranscribeResult(markdown, actions)
    }
}

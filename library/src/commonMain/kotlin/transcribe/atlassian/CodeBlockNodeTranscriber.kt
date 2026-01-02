package transcribe.atlassian

import data.atlassian.adf.CodeBlockNode
import data.atlassian.adf.TextNode
import transcribe.TranscribeResult

/**
 * Transcriber for CodeBlockNode that converts ADF code block to markdown fenced code block.
 * Outputs triple backticks with optional language identifier.
 */
class CodeBlockNodeTranscriber : ADFTranscriber<CodeBlockNode> {
    override fun transcribe(
        input: CodeBlockNode,
        context: ADFTranscriberContext,
    ): TranscribeResult<String> {
        val language = input.attrs?.language ?: ""

        val content = input.content
        val code =
            if (content.isNullOrEmpty()) {
                ""
            } else {
                content.filterIsInstance<TextNode>().joinToString("\n") { textNode ->
                    textNode.text
                }
            }

        val fence =
            if (language.isNotBlank()) {
                "```$language\n$code\n```"
            } else {
                "```\n$code\n```"
            }

        return TranscribeResult("$fence\n\n")
    }
}

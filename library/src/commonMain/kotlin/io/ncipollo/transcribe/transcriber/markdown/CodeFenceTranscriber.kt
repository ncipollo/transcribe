package io.ncipollo.transcribe.transcriber.markdown

import io.ncipollo.transcribe.context.MarkdownContext
import io.ncipollo.transcribe.data.atlassian.adf.CodeBlockAttrs
import io.ncipollo.transcribe.data.atlassian.adf.CodeBlockNode
import io.ncipollo.transcribe.data.atlassian.adf.TextNode
import io.ncipollo.transcribe.data.markdown.parser.getTextContent
import org.intellij.markdown.MarkdownTokenTypes
import org.intellij.markdown.ast.ASTNode
import org.intellij.markdown.ast.findChildOfType
import io.ncipollo.transcribe.transcriber.TranscribeResult

/**
 * Transcriber for CODE_FENCE nodes that converts markdown fenced code blocks to ADF CodeBlockNode.
 */
class CodeFenceTranscriber : MarkdownTranscriber<CodeBlockNode> {
    override fun transcribe(
        input: ASTNode,
        context: MarkdownContext,
    ): TranscribeResult<CodeBlockNode> {
        // Extract language from FENCE_LANG child if present
        val language: String? =
            input.findChildOfType(MarkdownTokenTypes.FENCE_LANG)
                ?.getTextContent(context.markdownText)
                ?.toString()
                ?.trim()

        // Extract code content by mapping child nodes
        val codeText =
            input.children.joinToString("") { child ->
                when (child.type.name) {
                    MarkdownTokenTypes.CODE_FENCE_CONTENT.name -> child.getTextContent(context.markdownText).toString()
                    MarkdownTokenTypes.EOL.name -> "\n"
                    else -> ""
                }
            }.trim('\n')

        return TranscribeResult(
            CodeBlockNode(
                attrs = CodeBlockAttrs(language = language?.takeIf { it.isNotEmpty() }),
                content = listOf(TextNode(text = codeText)),
            ),
        )
    }
}

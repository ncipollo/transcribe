package io.ncipollo.transcribe.transcriber.markdown

import io.ncipollo.transcribe.context.MarkdownContext
import io.ncipollo.transcribe.data.atlassian.adf.CodeBlockAttrs
import io.ncipollo.transcribe.data.atlassian.adf.CodeBlockNode
import io.ncipollo.transcribe.data.atlassian.adf.TextNode
import io.ncipollo.transcribe.data.markdown.parser.getTextContent
import org.intellij.markdown.MarkdownTokenTypes
import org.intellij.markdown.ast.ASTNode
import io.ncipollo.transcribe.transcriber.TranscribeResult

/**
 * Transcriber for CODE_BLOCK nodes that converts markdown indented code blocks to ADF CodeBlockNode.
 */
class CodeBlockTranscriber : MarkdownTranscriber<CodeBlockNode> {
    override fun transcribe(
        input: ASTNode,
        context: MarkdownContext,
    ): TranscribeResult<CodeBlockNode> {
        // Extract code content by mapping child nodes
        val codeText =
            input.children.joinToString("") { child ->
                when (child.type.name) {
                    MarkdownTokenTypes.CODE_LINE.name -> child.getTextContent(context.markdownText).toString().trimIndent()
                    MarkdownTokenTypes.EOL.name -> "\n"
                    else -> ""
                }
            }

        return TranscribeResult(
            CodeBlockNode(
                attrs = CodeBlockAttrs(language = null),
                content = listOf(TextNode(text = codeText)),
            ),
        )
    }
}

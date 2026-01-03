package transcribe.markdown

import context.MarkdownContext
import data.atlassian.adf.CodeBlockAttrs
import data.atlassian.adf.CodeBlockNode
import data.atlassian.adf.TextNode
import data.markdown.parser.getTextContent
import org.intellij.markdown.MarkdownTokenTypes
import org.intellij.markdown.ast.ASTNode
import transcribe.TranscribeResult

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

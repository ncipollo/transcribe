package transcribe.markdown

import data.atlassian.adf.CodeBlockAttrs
import data.atlassian.adf.CodeBlockNode
import data.atlassian.adf.TextNode
import data.markdown.parser.getTextContent
import org.intellij.markdown.MarkdownTokenTypes
import org.intellij.markdown.ast.ASTNode
import org.intellij.markdown.ast.findChildOfType
import transcribe.TranscribeResult

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


package transcribe.markdown

import data.atlassian.adf.CodeBlockAttrs
import data.atlassian.adf.CodeBlockNode
import data.atlassian.adf.TextNode
import data.markdown.parser.getTextContent
import org.intellij.markdown.MarkdownElementTypes
import org.intellij.markdown.MarkdownTokenTypes
import org.intellij.markdown.ast.ASTNode
import org.intellij.markdown.ast.findChildOfType
import transcribe.TranscribeResult

/**
 * Transcriber for CODE_FENCE and CODE_BLOCK nodes that converts markdown code blocks to ADF CodeBlockNode.
 */
class CodeBlockTranscriber : MarkdownTranscriber<CodeBlockNode> {
    override fun transcribe(
        input: ASTNode,
        context: MarkdownContext,
    ): TranscribeResult<CodeBlockNode> {
        val language: String? = if (input.type == MarkdownElementTypes.CODE_FENCE) {
            // Extract language from FENCE_LANG child
            val fenceLangNode = input.findChildOfType(MarkdownTokenTypes.FENCE_LANG)
            if (fenceLangNode != null) {
                fenceLangNode.getTextContent(context.markdownText).toString().trim()
            } else {
                null
            }
        } else {
            null
        }

        // Extract code content from CODE_FENCE_CONTENT or CODE_BLOCK content
        val contentNode = if (input.type == MarkdownElementTypes.CODE_FENCE) {
            input.findChildOfType(MarkdownElementTypes.CODE_FENCE)
        } else {
            // For CODE_BLOCK, use the node itself
            input
        }

        val codeText = if (contentNode != null) {
            contentNode.getTextContent(context.markdownText).toString()
        } else {
            ""
        }

        // Split by lines and create TextNode for each line
        val textNodes = codeText.lines().map { line ->
            TextNode(text = line)
        }

        return TranscribeResult(
            CodeBlockNode(
                attrs = CodeBlockAttrs(language = language?.takeIf { it.isNotEmpty() }),
                content = textNodes,
            ),
        )
    }
}


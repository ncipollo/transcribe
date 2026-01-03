package transcribe.markdown

import context.MarkdownContext
import data.atlassian.adf.ADFBlockNode
import data.atlassian.adf.ParagraphNode
import data.markdown.parser.getTextContent
import org.intellij.markdown.ast.ASTNode
import transcribe.TranscribeResult

/**
 * Router transcriber for HTML_BLOCK elements that delegates to specialized handlers
 * based on the HTML content type.
 */
class HtmlBlockTranscriber(
    private val nodeMapper: MarkdownNodeMapper,
) : MarkdownTranscriber<ADFBlockNode> {
    private val detailsTranscriber = DetailsTranscriber(nodeMapper)

    override fun transcribe(
        input: ASTNode,
        context: MarkdownContext,
    ): TranscribeResult<ADFBlockNode> {
        // Extract raw HTML text from the AST node
        val htmlText = input.getTextContent(context.markdownText).toString().trim()

        // Route to appropriate specialized transcriber based on HTML content
        when {
            htmlText.startsWith("<details", ignoreCase = true) -> {
                val result = detailsTranscriber.transcribe(input, context)
                return TranscribeResult(result.content as ADFBlockNode)
            }
            else -> {
                // For now, return empty content for unrecognized HTML blocks
                // In the future, we could add a generic HTML transcriber or other handlers
                return TranscribeResult(
                    ParagraphNode(
                        content = emptyList(),
                    ),
                )
            }
        }
    }
}

package transcribe.markdown

import context.MarkdownContext
import data.atlassian.adf.ExpandAttrs
import data.atlassian.adf.ExpandNode
import data.markdown.parser.MarkdownDocument
import data.markdown.parser.getTextContent
import org.intellij.markdown.ast.ASTNode
import transcribe.TranscribeResult

/**
 * Transcriber for HTML details elements that converts them to ADF ExpandNode.
 * Parses the HTML to extract summary and content, then recursively transcribes
 * the inner markdown content.
 */
class DetailsTranscriber(
    private val nodeMapper: MarkdownNodeMapper,
) : MarkdownTranscriber<ExpandNode> {
    override fun transcribe(
        input: ASTNode,
        context: MarkdownContext,
    ): TranscribeResult<ExpandNode> {
        // Extract raw HTML text from the AST node
        val htmlText = input.getTextContent(context.markdownText).toString()

        // Parse the details HTML
        val parsed = DetailsHtmlParser.parse(htmlText)
            ?: return TranscribeResult(
                ExpandNode(
                    content = emptyList(),
                    attrs = ExpandAttrs(title = "Invalid details element"),
                ),
            )

        // Parse the inner markdown content into an AST tree
        val innerDocument = MarkdownDocument.create(parsed.content)
        val innerContext = context.copy(markdownText = parsed.content)

        // Transcribe the inner content using the mapper
        val innerResult = nodeMapper.transcribeBlockChildren(innerDocument.rootNode, innerContext)

        return TranscribeResult(
            ExpandNode(
                content = innerResult.content,
                attrs = ExpandAttrs(title = parsed.summary),
            ),
            innerResult.actions,
        )
    }
}

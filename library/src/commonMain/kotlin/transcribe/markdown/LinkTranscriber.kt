package transcribe.markdown

import data.atlassian.adf.LinkAttrs
import data.atlassian.adf.LinkMark
import data.atlassian.adf.TextNode
import data.markdown.parser.getTextContent
import org.intellij.markdown.MarkdownElementTypes
import org.intellij.markdown.ast.ASTNode
import org.intellij.markdown.ast.findChildOfType
import transcribe.TranscribeResult

/**
 * Transcriber for INLINE_LINK, FULL_REFERENCE_LINK, SHORT_REFERENCE_LINK, and AUTOLINK nodes
 * that converts markdown links to ADF TextNode with LinkMark.
 */
class LinkTranscriber(
    private val inlineTranscriber: InlineContentTranscriber,
) : MarkdownTranscriber<TextNode> {
    override fun transcribe(
        input: ASTNode,
        context: MarkdownContext,
    ): TranscribeResult<TextNode> {
        when (input.type) {
            MarkdownElementTypes.AUTOLINK -> {
                // Autolink: extract URL directly from text
                val url =
                    input.getTextContent(context.markdownText).toString()
                        .removePrefix("<")
                        .removeSuffix(">")
                val text = url

                return TranscribeResult(
                    TextNode(
                        text = text,
                        marks = listOf(LinkMark(LinkAttrs(href = url))),
                    ),
                )
            }
            MarkdownElementTypes.INLINE_LINK -> {
                // Inline link: [text](url)
                val linkTextNode = input.findChildOfType(MarkdownElementTypes.LINK_TEXT)
                val destinationNode = input.findChildOfType(MarkdownElementTypes.LINK_DESTINATION)

                val text =
                    if (linkTextNode != null) {
                        inlineTranscriber.transcribeChildren(linkTextNode, context)
                            .joinToString("") { node ->
                                if (node is TextNode) node.text else ""
                            }
                    } else {
                        ""
                    }

                val href =
                    if (destinationNode != null) {
                        destinationNode.getTextContent(context.markdownText).toString()
                            .removePrefix("(")
                            .removeSuffix(")")
                    } else {
                        ""
                    }

                return TranscribeResult(
                    TextNode(
                        text = text,
                        marks = listOf(LinkMark(LinkAttrs(href = href))),
                    ),
                )
            }
            MarkdownElementTypes.FULL_REFERENCE_LINK,
            MarkdownElementTypes.SHORT_REFERENCE_LINK,
            -> {
                // Reference links: [text][ref] or [text]
                // For now, extract text only (reference resolution would need link definitions)
                val linkTextNode = input.findChildOfType(MarkdownElementTypes.LINK_TEXT)
                val text =
                    if (linkTextNode != null) {
                        inlineTranscriber.transcribeChildren(linkTextNode, context)
                            .joinToString("") { node ->
                                if (node is TextNode) node.text else ""
                            }
                    } else {
                        ""
                    }

                // TODO: Resolve reference link destination from link definitions
                // For now, use text as href
                return TranscribeResult(
                    TextNode(
                        text = text,
                        marks = listOf(LinkMark(LinkAttrs(href = text))),
                    ),
                )
            }
            else -> {
                // Fallback
                val text = input.getTextContent(context.markdownText).toString()
                return TranscribeResult(TextNode(text = text))
            }
        }
    }
}

package io.ncipollo.transcribe.transcriber.markdown

import io.ncipollo.transcribe.context.MarkdownContext
import io.ncipollo.transcribe.data.atlassian.adf.LinkAttrs
import io.ncipollo.transcribe.data.atlassian.adf.LinkMark
import io.ncipollo.transcribe.data.atlassian.adf.TextNode
import io.ncipollo.transcribe.data.markdown.parser.findChildOfTypeInSubtree
import io.ncipollo.transcribe.data.markdown.parser.getTextContent
import io.ncipollo.transcribe.data.markdown.parser.getTextContentWithoutDelimiters
import org.intellij.markdown.MarkdownElementTypes
import org.intellij.markdown.ast.ASTNode
import org.intellij.markdown.flavours.gfm.GFMTokenTypes
import io.ncipollo.transcribe.transcriber.TranscribeResult

/**
 * Transcriber for INLINE_LINK, AUTOLINK, and GFM_AUTOLINK nodes
 * that converts markdown links to ADF TextNode with LinkMark.
 */
class LinkTranscriber : MarkdownTranscriber<TextNode> {
    override fun transcribe(
        input: ASTNode,
        context: MarkdownContext,
    ): TranscribeResult<TextNode> {
        when (input.type) {
            MarkdownElementTypes.AUTOLINK -> {
                // Autolink: extract URL directly from text
                val url = input.getTextContentWithoutDelimiters(context.markdownText, "<", ">")
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
                val text =
                    input.findChildOfTypeInSubtree(MarkdownElementTypes.LINK_TEXT)
                        ?.getTextContentWithoutDelimiters(context.markdownText, "[", "]") ?: ""
                val url =
                    input.findChildOfTypeInSubtree(MarkdownElementTypes.LINK_DESTINATION)
                        ?.getTextContent(context.markdownText)
                        ?.toString() ?: ""

                return TranscribeResult(
                    TextNode(
                        text = text,
                        marks = listOf(LinkMark(LinkAttrs(href = url))),
                    ),
                )
            }
            GFMTokenTypes.GFM_AUTOLINK -> {
                // GFM autolink: treat as plain text (no link mark)
                val text = input.getTextContent(context.markdownText).toString()
                return TranscribeResult(TextNode(text = text))
            }
            else -> {
                // Fallback
                val text = input.getTextContent(context.markdownText).toString()
                return TranscribeResult(TextNode(text = text))
            }
        }
    }
}

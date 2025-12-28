package transcribe.markdown

import data.atlassian.adf.CaptionNode
import data.atlassian.adf.MediaAttrs
import data.atlassian.adf.MediaNode
import data.atlassian.adf.MediaSingleAttrs
import data.atlassian.adf.MediaSingleLayout
import data.atlassian.adf.MediaSingleNode
import data.atlassian.adf.MediaType
import data.atlassian.adf.TextNode
import data.markdown.parser.getTextContent
import org.intellij.markdown.MarkdownElementTypes
import org.intellij.markdown.ast.ASTNode
import org.intellij.markdown.ast.findChildOfType
import transcribe.TranscribeResult

/**
 * Transcriber for IMAGE nodes that converts markdown images to ADF MediaSingleNode with MediaNode.
 */
class ImageTranscriber(
    private val mapper: MarkdownNodeMapper,
) : MarkdownTranscriber<MediaSingleNode> {
    override fun transcribe(
        input: ASTNode,
        context: MarkdownContext,
    ): TranscribeResult<MediaSingleNode> {
        // IMAGE node structure: IMAGE -> INLINE_LINK -> LINK_TEXT, LINK_DESTINATION
        val inlineLinkNode =
            input.findChildOfType(MarkdownElementTypes.INLINE_LINK)
                ?: return TranscribeResult(
                    MediaSingleNode(
                        content = emptyList(),
                        attrs = MediaSingleAttrs(layout = MediaSingleLayout.CENTER),
                    ),
                )

        val linkTextNode = inlineLinkNode.findChildOfType(MarkdownElementTypes.LINK_TEXT)
        val destinationNode = inlineLinkNode.findChildOfType(MarkdownElementTypes.LINK_DESTINATION)

        val altText =
            if (linkTextNode != null) {
                mapper.transcribeInlineChildren(linkTextNode, context)
                    .joinToString("") { node ->
                        if (node is data.atlassian.adf.TextNode) node.text else ""
                    }
            } else {
                ""
            }

        val url =
            if (destinationNode != null) {
                destinationNode.getTextContent(context.markdownText).toString()
                    .removePrefix("(")
                    .removeSuffix(")")
            } else {
                ""
            }

        // Create MediaNode with external type
        val mediaNode =
            MediaNode(
                attrs =
                    MediaAttrs(
                        type = MediaType.EXTERNAL,
                        url = url,
                        alt = altText.takeIf { it.isNotEmpty() },
                    ),
            )

        // Optionally create caption from alt text
        val captionNode =
            if (altText.isNotEmpty()) {
                CaptionNode(
                    content = listOf(TextNode(text = altText)),
                )
            } else {
                null
            }

        val content =
            if (captionNode != null) {
                listOf(mediaNode, captionNode)
            } else {
                listOf(mediaNode)
            }

        return TranscribeResult(
            MediaSingleNode(
                content = content,
                attrs = MediaSingleAttrs(layout = MediaSingleLayout.CENTER),
            ),
        )
    }
}

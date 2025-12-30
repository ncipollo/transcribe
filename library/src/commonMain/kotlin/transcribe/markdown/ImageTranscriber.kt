package transcribe.markdown

import data.atlassian.adf.CaptionNode
import data.atlassian.adf.MediaAttrs
import data.atlassian.adf.MediaNode
import data.atlassian.adf.MediaSingleAttrs
import data.atlassian.adf.MediaSingleLayout
import data.atlassian.adf.MediaSingleNode
import data.atlassian.adf.MediaType
import data.atlassian.adf.TextNode
import data.markdown.parser.findChildOfTypeInSubtree
import data.markdown.parser.findTextContent
import data.markdown.parser.getTextContent
import org.intellij.markdown.MarkdownElementTypes
import org.intellij.markdown.ast.ASTNode
import transcribe.TranscribeResult

/**
 * Transcriber for IMAGE nodes that converts markdown images to ADF MediaSingleNode with MediaNode.
 */
class ImageTranscriber : MarkdownTranscriber<MediaSingleNode> {
    override fun transcribe(
        input: ASTNode,
        context: MarkdownContext,
    ): TranscribeResult<MediaSingleNode> {
        val altText =
            input.findChildOfTypeInSubtree(MarkdownElementTypes.LINK_TEXT)
                ?.findTextContent(context.markdownText) ?: ""
        val url =
            input.findChildOfTypeInSubtree(MarkdownElementTypes.LINK_DESTINATION)
                ?.getTextContent(context.markdownText)
                ?.toString() ?: ""

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
            altText.takeIf { it.isNotEmpty() }?.let {
                CaptionNode(
                    content = listOf(TextNode(text = it)),
                )
            }

        val content = listOfNotNull(mediaNode, captionNode)

        return TranscribeResult(
            MediaSingleNode(
                content = content,
                attrs = MediaSingleAttrs(layout = MediaSingleLayout.CENTER),
            ),
        )
    }
}

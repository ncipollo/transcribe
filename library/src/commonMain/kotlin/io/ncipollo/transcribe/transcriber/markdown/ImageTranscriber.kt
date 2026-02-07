package io.ncipollo.transcribe.transcriber.markdown

import io.ncipollo.transcribe.context.MarkdownContext
import io.ncipollo.transcribe.data.atlassian.adf.CaptionNode
import io.ncipollo.transcribe.data.atlassian.adf.MediaAttrs
import io.ncipollo.transcribe.data.atlassian.adf.MediaNode
import io.ncipollo.transcribe.data.atlassian.adf.MediaSingleAttrs
import io.ncipollo.transcribe.data.atlassian.adf.MediaSingleLayout
import io.ncipollo.transcribe.data.atlassian.adf.MediaSingleNode
import io.ncipollo.transcribe.data.atlassian.adf.MediaType
import io.ncipollo.transcribe.data.atlassian.adf.TextNode
import io.ncipollo.transcribe.data.markdown.parser.findChildOfTypeInSubtree
import io.ncipollo.transcribe.data.markdown.parser.getTextContent
import io.ncipollo.transcribe.data.markdown.parser.getTextContentWithoutDelimiters
import org.intellij.markdown.MarkdownElementTypes
import org.intellij.markdown.ast.ASTNode
import io.ncipollo.transcribe.transcriber.TranscribeResult

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
                ?.getTextContentWithoutDelimiters(context.markdownText, "[", "]") ?: ""
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

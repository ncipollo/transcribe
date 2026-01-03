package transcribe.markdown

import data.atlassian.adf.CaptionNode
import data.atlassian.adf.MediaAttrs
import data.atlassian.adf.MediaNode
import data.atlassian.adf.MediaSingleAttrs
import data.atlassian.adf.MediaSingleLayout
import data.atlassian.adf.MediaSingleNode
import data.atlassian.adf.MediaType
import data.atlassian.adf.TextNode
import org.intellij.markdown.MarkdownElementTypes
import kotlin.test.Test
import kotlin.test.assertEquals

class ImageTranscriberTest {
    private val transcriber = ImageTranscriber()

    @Test
    fun transcribe_basicImage_fullURL() {
        val markdown = "![alt text](https://example.com/image.png)"
        val imageNode = MarkdownTestHelper.findNode(markdown, MarkdownElementTypes.IMAGE)
        val context = MarkdownContext(markdownText = markdown)
        val result = transcriber.transcribe(imageNode, context)

        val expected =
            MediaSingleNode(
                attrs = MediaSingleAttrs(layout = MediaSingleLayout.CENTER),
                content =
                listOf(
                    MediaNode(
                        attrs =
                        MediaAttrs(
                            type = MediaType.EXTERNAL,
                            url = "https://example.com/image.png",
                            alt = "alt text",
                        ),
                    ),
                    CaptionNode(
                        content = listOf(TextNode(text = "alt text")),
                    ),
                ),
            )
        assertEquals(expected, result.content)
    }

    @Test
    fun transcribe_basicImage_relative() {
        val markdown = "![alt text](image.png)"
        val imageNode = MarkdownTestHelper.findNode(markdown, MarkdownElementTypes.IMAGE)
        val context = MarkdownContext(markdownText = markdown)
        val result = transcriber.transcribe(imageNode, context)

        val expected =
            MediaSingleNode(
                attrs = MediaSingleAttrs(layout = MediaSingleLayout.CENTER),
                content =
                listOf(
                    MediaNode(
                        attrs =
                        MediaAttrs(
                            type = MediaType.EXTERNAL,
                            url = "image.png",
                            alt = "alt text",
                        ),
                    ),
                    CaptionNode(
                        content = listOf(TextNode(text = "alt text")),
                    ),
                ),
            )
        assertEquals(expected, result.content)
    }

    @Test
    fun transcribe_imageWithoutAltText() {
        val markdown = "![](https://example.com/image.png)"
        val imageNode = MarkdownTestHelper.findNode(markdown, MarkdownElementTypes.IMAGE)
        val context = MarkdownContext(markdownText = markdown)
        val result = transcriber.transcribe(imageNode, context)

        val expected =
            MediaSingleNode(
                attrs = MediaSingleAttrs(layout = MediaSingleLayout.CENTER),
                content =
                listOf(
                    MediaNode(
                        attrs =
                        MediaAttrs(
                            type = MediaType.EXTERNAL,
                            url = "https://example.com/image.png",
                            alt = null,
                        ),
                    ),
                ),
            )
        assertEquals(expected, result.content)
    }
}

package transcribe.markdown

import context.MarkdownContext
import data.atlassian.adf.LinkAttrs
import data.atlassian.adf.LinkMark
import data.atlassian.adf.TextNode
import org.intellij.markdown.MarkdownElementTypes
import org.intellij.markdown.flavours.gfm.GFMTokenTypes
import kotlin.test.Test
import kotlin.test.assertEquals

class LinkTranscriberTest {
    private val transcriber = LinkTranscriber()

    @Test
    fun transcribe_inlineLink_basicUrl() {
        val markdown = "[text](https://example.com)"
        val linkNode = MarkdownTestHelper.findNode(markdown, MarkdownElementTypes.INLINE_LINK)
        val context = MarkdownContext(markdownText = markdown)
        val result = transcriber.transcribe(linkNode, context)

        val expected =
            TextNode(
                text = "text",
                marks = listOf(LinkMark(LinkAttrs(href = "https://example.com"))),
            )
        assertEquals(expected, result.content)
    }

    @Test
    fun transcribe_inlineLink_relativeUrl() {
        val markdown = "[text](page.html)"
        val linkNode = MarkdownTestHelper.findNode(markdown, MarkdownElementTypes.INLINE_LINK)
        val context = MarkdownContext(markdownText = markdown)
        val result = transcriber.transcribe(linkNode, context)

        val expected =
            TextNode(
                text = "text",
                marks = listOf(LinkMark(LinkAttrs(href = "page.html"))),
            )
        assertEquals(expected, result.content)
    }

    @Test
    fun transcribe_inlineLink_emptyText() {
        val markdown = "[](https://example.com)"
        val linkNode = MarkdownTestHelper.findNode(markdown, MarkdownElementTypes.INLINE_LINK)
        val context = MarkdownContext(markdownText = markdown)
        val result = transcriber.transcribe(linkNode, context)

        val expected =
            TextNode(
                text = "",
                marks = listOf(LinkMark(LinkAttrs(href = "https://example.com"))),
            )
        assertEquals(expected, result.content)
    }

    @Test
    fun transcribe_autolink() {
        val markdown = "<https://example.com>"
        val linkNode = MarkdownTestHelper.findNode(markdown, MarkdownElementTypes.AUTOLINK)
        val context = MarkdownContext(markdownText = markdown)
        val result = transcriber.transcribe(linkNode, context)

        val expected =
            TextNode(
                text = "https://example.com",
                marks = listOf(LinkMark(LinkAttrs(href = "https://example.com"))),
            )
        assertEquals(expected, result.content)
    }

    @Test
    fun transcribe_gfmAutolink_treatedAsText() {
        val markdown = "https://example.com"
        val linkNode = MarkdownTestHelper.findNode(markdown, GFMTokenTypes.GFM_AUTOLINK)
        val context = MarkdownContext(markdownText = markdown)
        val result = transcriber.transcribe(linkNode, context)

        val expected = TextNode(text = "https://example.com")
        assertEquals(expected, result.content)
    }
}

package transcribe.markdown

import data.atlassian.adf.ExpandNode
import data.atlassian.adf.ParagraphNode
import org.intellij.markdown.MarkdownElementTypes
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class HtmlBlockTranscriberTest {
    private val nodeMapper = defaultMarkdownNodeMapper()
    private val transcriber = HtmlBlockTranscriber(nodeMapper)

    @Test
    fun transcribe_detailsBlock_routesToDetailsTranscriber() {
        val markdown = "<details><summary>Collapse</summary>- Content which is kinda hidden.</details>"
        val htmlBlockNode = MarkdownTestHelper.findNode(markdown, MarkdownElementTypes.HTML_BLOCK)
        val context = MarkdownContext(markdownText = markdown)
        val result = transcriber.transcribe(htmlBlockNode, context)

        assertTrue(result.content is ExpandNode)
        val expandNode = result.content as ExpandNode
        assertEquals("Collapse", expandNode.attrs?.title)
    }

    @Test
    fun transcribe_otherHtmlBlock_returnsEmptyParagraph() {
        val markdown = "<div>Some content</div>"
        val htmlBlockNode = MarkdownTestHelper.findNode(markdown, MarkdownElementTypes.HTML_BLOCK)
        val context = MarkdownContext(markdownText = markdown)
        val result = transcriber.transcribe(htmlBlockNode, context)

        assertTrue(result.content is ParagraphNode)
        val paragraphNode = result.content as ParagraphNode
        assertEquals(emptyList(), paragraphNode.content)
    }
}


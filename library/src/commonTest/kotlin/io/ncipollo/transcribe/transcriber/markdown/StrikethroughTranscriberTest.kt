package io.ncipollo.transcribe.transcriber.markdown

import io.ncipollo.transcribe.context.MarkdownContext
import io.ncipollo.transcribe.data.atlassian.adf.StrikeMark
import io.ncipollo.transcribe.data.atlassian.adf.TextNode
import org.intellij.markdown.flavours.gfm.GFMElementTypes
import kotlin.test.Test
import kotlin.test.assertEquals

class StrikethroughTranscriberTest {
    private val transcriber = StrikethroughTranscriber()

    @Test
    fun transcribe_strikethroughText() {
        val markdown = "~~strikethrough~~"
        val strikethroughNode = MarkdownTestHelper.findNestedNode(markdown, GFMElementTypes.STRIKETHROUGH)
        val context = MarkdownContext(markdownText = markdown)
        val result = transcriber.transcribe(strikethroughNode, context)

        val expected = TextNode(text = "strikethrough", marks = listOf(StrikeMark))
        assertEquals(expected, result.content)
    }
}

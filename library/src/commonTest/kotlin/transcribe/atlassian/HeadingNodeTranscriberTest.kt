package transcribe.atlassian

import data.atlassian.adf.HeadingAttrs
import data.atlassian.adf.HeadingNode
import data.atlassian.adf.TextNode
import kotlin.test.Test
import kotlin.test.assertEquals

class HeadingNodeTranscriberTest {

    private val transcriber = HeadingNodeTranscriber(defaultADFNodeMapper())

    @Test
    fun transcribe_level1() {
        val node = HeadingNode(
            attrs = HeadingAttrs(level = 1),
            content = listOf(TextNode(text = "Title"))
        )
        val result = transcriber.transcribe(node)
        assertEquals("# Title\n\n", result.content)
    }

    @Test
    fun transcribe_level3() {
        val node = HeadingNode(
            attrs = HeadingAttrs(level = 3),
            content = listOf(TextNode(text = "Subtitle"))
        )
        val result = transcriber.transcribe(node)
        assertEquals("### Subtitle\n\n", result.content)
    }

    @Test
    fun transcribe_level6() {
        val node = HeadingNode(
            attrs = HeadingAttrs(level = 6),
            content = listOf(TextNode(text = "Smallest"))
        )
        val result = transcriber.transcribe(node)
        assertEquals("###### Smallest\n\n", result.content)
    }

    @Test
    fun transcribe_emptyContent() {
        val node = HeadingNode(
            attrs = HeadingAttrs(level = 2),
            content = null
        )
        val result = transcriber.transcribe(node)
        assertEquals("## \n\n", result.content)
    }
}


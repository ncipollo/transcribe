package transcribe.atlassian

import data.atlassian.adf.ParagraphNode
import data.atlassian.adf.TextNode
import kotlin.test.Test
import kotlin.test.assertEquals

class ParagraphNodeTranscriberTest {

    private val transcriber = ParagraphNodeTranscriber(defaultADFNodeMapper())

    @Test
    fun transcribe_withContent() {
        val node = ParagraphNode(
            content = listOf(
                TextNode(text = "Hello"),
                TextNode(text = " world")
            )
        )
        val result = transcriber.transcribe(node)
        assertEquals("Hello world\n", result.content)
    }

    @Test
    fun transcribe_emptyContent() {
        val node = ParagraphNode(content = null)
        val result = transcriber.transcribe(node)
        assertEquals("\n", result.content)
    }

    @Test
    fun transcribe_emptyList() {
        val node = ParagraphNode(content = emptyList())
        val result = transcriber.transcribe(node)
        assertEquals("\n", result.content)
    }
}


package transcribe.atlassian

import data.atlassian.adf.ParagraphNode
import data.atlassian.adf.TableHeaderNode
import data.atlassian.adf.TextNode
import kotlin.test.Test
import kotlin.test.assertEquals

class TableHeaderNodeTranscriberTest {

    private val transcriber = TableHeaderNodeTranscriber()

    @Test
    fun transcribe_withContent() {
        val node = TableHeaderNode(
            content = listOf(
                ParagraphNode(content = listOf(TextNode(text = "Header")))
            )
        )
        val result = transcriber.transcribe(node)
        assertEquals("Header", result.content)
    }

    @Test
    fun transcribe_emptyContent() {
        val node = TableHeaderNode(content = emptyList())
        val result = transcriber.transcribe(node)
        assertEquals("", result.content)
    }
}


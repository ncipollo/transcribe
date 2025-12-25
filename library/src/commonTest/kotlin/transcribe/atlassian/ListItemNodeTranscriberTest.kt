package transcribe.atlassian

import data.atlassian.adf.ListItemNode
import data.atlassian.adf.ParagraphNode
import data.atlassian.adf.TextNode
import kotlin.test.Test
import kotlin.test.assertEquals

class ListItemNodeTranscriberTest {

    private val transcriber = ListItemNodeTranscriber(defaultADFNodeMapper())

    @Test
    fun transcribe_withInlineContent() {
        val node = ListItemNode(
            content = listOf(
                ParagraphNode(content = listOf(TextNode(text = "Item text")))
            )
        )
        val result = transcriber.transcribe(node)
        assertEquals("Item text\n", result.content)
    }

    @Test
    fun transcribe_emptyContent() {
        val node = ListItemNode(content = emptyList())
        val result = transcriber.transcribe(node)
        assertEquals("", result.content)
    }

    @Test
    fun transcribe_multipleParagraphs() {
        val node = ListItemNode(
            content = listOf(
                ParagraphNode(content = listOf(TextNode(text = "First"))),
                ParagraphNode(content = listOf(TextNode(text = "Second")))
            )
        )
        val result = transcriber.transcribe(node)
        assertEquals("First\nSecond\n", result.content)
    }
}


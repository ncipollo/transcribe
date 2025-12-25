package transcribe.atlassian

import data.atlassian.adf.BlockquoteNode
import data.atlassian.adf.ParagraphNode
import data.atlassian.adf.TextNode
import kotlin.test.Test
import kotlin.test.assertEquals

class BlockquoteNodeTranscriberTest {
    private val transcriber = BlockquoteNodeTranscriber(defaultADFNodeMapper())

    @Test
    fun transcribe_withContent() {
        val node =
            BlockquoteNode(
                content =
                    listOf(
                        ParagraphNode(content = listOf(TextNode(text = "Quoted text"))),
                    ),
            )
        val result = transcriber.transcribe(node)
        assertEquals("> Quoted text\n\n", result.content)
    }

    @Test
    fun transcribe_emptyContent() {
        val node = BlockquoteNode(content = emptyList())
        val result = transcriber.transcribe(node)
        assertEquals("", result.content)
    }

    @Test
    fun transcribe_multipleParagraphs() {
        val node =
            BlockquoteNode(
                content =
                    listOf(
                        ParagraphNode(content = listOf(TextNode(text = "First"))),
                        ParagraphNode(content = listOf(TextNode(text = "Second"))),
                    ),
            )
        val result = transcriber.transcribe(node)
        assertEquals("> First\n> Second\n\n", result.content)
    }
}

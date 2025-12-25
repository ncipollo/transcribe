package transcribe.atlassian

import data.atlassian.adf.ParagraphNode
import data.atlassian.adf.TableCellNode
import data.atlassian.adf.TextNode
import kotlin.test.Test
import kotlin.test.assertEquals

class TableCellNodeTranscriberTest {
    private val transcriber = TableCellNodeTranscriber(defaultADFNodeMapper())
    private val context = ADFTranscriberContext()

    @Test
    fun transcribe_withContent() {
        val node =
            TableCellNode(
                content =
                    listOf(
                        ParagraphNode(content = listOf(TextNode(text = "Cell content"))),
                    ),
            )
        val result = transcriber.transcribe(node, context)
        assertEquals("Cell content", result.content)
    }

    @Test
    fun transcribe_emptyContent() {
        val node = TableCellNode(content = emptyList())
        val result = transcriber.transcribe(node, context)
        assertEquals("", result.content)
    }
}

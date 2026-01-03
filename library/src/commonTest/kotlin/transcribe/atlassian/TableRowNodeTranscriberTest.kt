package transcribe.atlassian

import context.ADFTranscriberContext
import data.atlassian.adf.ParagraphNode
import data.atlassian.adf.TableCellNode
import data.atlassian.adf.TableHeaderNode
import data.atlassian.adf.TableRowNode
import data.atlassian.adf.TextNode
import kotlin.test.Test
import kotlin.test.assertEquals

class TableRowNodeTranscriberTest {
    private val transcriber = TableRowNodeTranscriber(defaultADFNodeMapper())
    private val context = ADFTranscriberContext()

    @Test
    fun transcribe_withCells() {
        val node =
            TableRowNode(
                content =
                listOf(
                    TableCellNode(
                        content =
                        listOf(
                            ParagraphNode(content = listOf(TextNode(text = "Cell 1"))),
                        ),
                    ),
                    TableCellNode(
                        content =
                        listOf(
                            ParagraphNode(content = listOf(TextNode(text = "Cell 2"))),
                        ),
                    ),
                ),
            )
        val result = transcriber.transcribe(node, context)
        assertEquals("| Cell 1 | Cell 2 |", result.content)
    }

    @Test
    fun transcribe_withHeaders() {
        val node =
            TableRowNode(
                content =
                listOf(
                    TableHeaderNode(
                        content =
                        listOf(
                            ParagraphNode(content = listOf(TextNode(text = "Header 1"))),
                        ),
                    ),
                    TableHeaderNode(
                        content =
                        listOf(
                            ParagraphNode(content = listOf(TextNode(text = "Header 2"))),
                        ),
                    ),
                ),
            )
        val result = transcriber.transcribe(node, context)
        assertEquals("| Header 1 | Header 2 |", result.content)
    }

    @Test
    fun transcribe_emptyContent() {
        val node = TableRowNode(content = emptyList())
        val result = transcriber.transcribe(node, context)
        assertEquals("", result.content)
    }
}

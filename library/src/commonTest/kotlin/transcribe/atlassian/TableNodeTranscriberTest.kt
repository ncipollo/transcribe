package transcribe.atlassian

import data.atlassian.adf.ParagraphNode
import data.atlassian.adf.TableCellNode
import data.atlassian.adf.TableHeaderNode
import data.atlassian.adf.TableNode
import data.atlassian.adf.TableRowNode
import data.atlassian.adf.TextNode
import kotlin.test.Test
import kotlin.test.assertEquals

class TableNodeTranscriberTest {

    private val transcriber = TableNodeTranscriber()

    @Test
    fun transcribe_withHeaders() {
        val node = TableNode(
            content = listOf(
                TableRowNode(
                    content = listOf(
                        TableHeaderNode(
                            content = listOf(
                                ParagraphNode(content = listOf(TextNode(text = "Header 1")))
                            )
                        ),
                        TableHeaderNode(
                            content = listOf(
                                ParagraphNode(content = listOf(TextNode(text = "Header 2")))
                            )
                        )
                    )
                ),
                TableRowNode(
                    content = listOf(
                        TableCellNode(
                            content = listOf(
                                ParagraphNode(content = listOf(TextNode(text = "Cell 1")))
                            )
                        ),
                        TableCellNode(
                            content = listOf(
                                ParagraphNode(content = listOf(TextNode(text = "Cell 2")))
                            )
                        )
                    )
                )
            )
        )
        val result = transcriber.transcribe(node)
        assertEquals("| Header 1 | Header 2 |\n| --- | --- |\n| Cell 1 | Cell 2 |\n\n", result.content)
    }

    @Test
    fun transcribe_withoutHeaders() {
        val node = TableNode(
            content = listOf(
                TableRowNode(
                    content = listOf(
                        TableCellNode(
                            content = listOf(
                                ParagraphNode(content = listOf(TextNode(text = "Cell 1")))
                            )
                        )
                    )
                )
            )
        )
        val result = transcriber.transcribe(node)
        assertEquals("| Cell 1 |\n\n", result.content)
    }

    @Test
    fun transcribe_emptyContent() {
        val node = TableNode(content = emptyList())
        val result = transcriber.transcribe(node)
        assertEquals("", result.content)
    }
}


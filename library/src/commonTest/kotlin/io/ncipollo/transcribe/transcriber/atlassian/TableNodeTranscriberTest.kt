package io.ncipollo.transcribe.transcriber.atlassian

import io.ncipollo.transcribe.context.ADFTranscriberContext
import io.ncipollo.transcribe.data.atlassian.adf.ParagraphNode
import io.ncipollo.transcribe.data.atlassian.adf.TableCellNode
import io.ncipollo.transcribe.data.atlassian.adf.TableHeaderNode
import io.ncipollo.transcribe.data.atlassian.adf.TableNode
import io.ncipollo.transcribe.data.atlassian.adf.TableRowNode
import io.ncipollo.transcribe.data.atlassian.adf.TextNode
import io.ncipollo.transcribe.transcriber.TranscribeResult
import kotlin.test.Test
import kotlin.test.assertEquals

class TableNodeTranscriberTest {
    private val transcriber = TableNodeTranscriber(defaultADFNodeMapper())
    private val context = ADFTranscriberContext()

    @Test
    fun transcribe_withHeaders() {
        val node =
            TableNode(
                content =
                listOf(
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
                    ),
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
                    ),
                ),
            )
        val result = transcriber.transcribe(node, context)
        val expected = TranscribeResult("| Header 1 | Header 2 |\n| --- | --- |\n| Cell 1 | Cell 2 |")
        assertEquals(expected, result)
    }

    @Test
    fun transcribe_withoutHeaders() {
        val node =
            TableNode(
                content =
                listOf(
                    TableRowNode(
                        content =
                        listOf(
                            TableCellNode(
                                content =
                                listOf(
                                    ParagraphNode(content = listOf(TextNode(text = "Cell 1"))),
                                ),
                            ),
                        ),
                    ),
                ),
            )
        val result = transcriber.transcribe(node, context)
        val expected = TranscribeResult("| Cell 1 |")
        assertEquals(expected, result)
    }

    @Test
    fun transcribe_emptyContent() {
        val node = TableNode(content = emptyList())
        val result = transcriber.transcribe(node, context)
        val expected = TranscribeResult("")
        assertEquals(expected, result)
    }
}

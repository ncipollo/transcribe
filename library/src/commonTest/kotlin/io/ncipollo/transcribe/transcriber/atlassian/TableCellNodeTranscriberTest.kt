package io.ncipollo.transcribe.transcriber.atlassian

import io.ncipollo.transcribe.context.ADFTranscriberContext
import io.ncipollo.transcribe.data.atlassian.adf.ParagraphNode
import io.ncipollo.transcribe.data.atlassian.adf.TableCellNode
import io.ncipollo.transcribe.data.atlassian.adf.TextNode
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

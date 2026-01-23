package io.ncipollo.transcribe.transcriber.atlassian

import io.ncipollo.transcribe.context.ADFTranscriberContext
import io.ncipollo.transcribe.data.atlassian.adf.ParagraphNode
import io.ncipollo.transcribe.data.atlassian.adf.TableHeaderNode
import io.ncipollo.transcribe.data.atlassian.adf.TextNode
import kotlin.test.Test
import kotlin.test.assertEquals

class TableHeaderNodeTranscriberTest {
    private val transcriber = TableHeaderNodeTranscriber(defaultADFNodeMapper())
    private val context = ADFTranscriberContext()

    @Test
    fun transcribe_withContent() {
        val node =
            TableHeaderNode(
                content =
                listOf(
                    ParagraphNode(content = listOf(TextNode(text = "Header"))),
                ),
            )
        val result = transcriber.transcribe(node, context)
        assertEquals("Header", result.content)
    }

    @Test
    fun transcribe_emptyContent() {
        val node = TableHeaderNode(content = emptyList())
        val result = transcriber.transcribe(node, context)
        assertEquals("", result.content)
    }
}

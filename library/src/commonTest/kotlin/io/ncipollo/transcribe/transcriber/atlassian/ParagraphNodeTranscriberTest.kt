package io.ncipollo.transcribe.transcriber.atlassian

import io.ncipollo.transcribe.context.ADFTranscriberContext
import io.ncipollo.transcribe.data.atlassian.adf.ParagraphNode
import io.ncipollo.transcribe.data.atlassian.adf.TextNode
import kotlin.test.Test
import kotlin.test.assertEquals

class ParagraphNodeTranscriberTest {
    private val transcriber = ParagraphNodeTranscriber(defaultADFNodeMapper())
    private val context = ADFTranscriberContext()

    @Test
    fun transcribe_withContent() {
        val node =
            ParagraphNode(
                content =
                listOf(
                    TextNode(text = "Hello"),
                    TextNode(text = " world"),
                ),
            )
        val result = transcriber.transcribe(node, context)
        assertEquals("Hello world\n", result.content)
    }

    @Test
    fun transcribe_emptyContent() {
        val node = ParagraphNode(content = null)
        val result = transcriber.transcribe(node, context)
        assertEquals("\n", result.content)
    }

    @Test
    fun transcribe_emptyList() {
        val node = ParagraphNode(content = emptyList())
        val result = transcriber.transcribe(node, context)
        assertEquals("\n", result.content)
    }
}

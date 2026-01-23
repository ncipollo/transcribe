package io.ncipollo.transcribe.transcriber.atlassian

import io.ncipollo.transcribe.context.ADFTranscriberContext
import io.ncipollo.transcribe.data.atlassian.adf.StrongMark
import io.ncipollo.transcribe.data.atlassian.adf.TextNode
import kotlin.test.Test
import kotlin.test.assertEquals

class TextNodeTranscriberTest {
    private val transcriber = TextNodeTranscriber()
    private val context = ADFTranscriberContext()

    @Test
    fun transcribe_withMark() {
        val node = TextNode(text = "hello", marks = listOf(StrongMark))
        val result = transcriber.transcribe(node, context)
        assertEquals("**hello**", result.content)
    }

    @Test
    fun transcribe_noMarks() {
        val node = TextNode(text = "hello")
        val result = transcriber.transcribe(node, context)
        assertEquals("hello", result.content)
    }
}

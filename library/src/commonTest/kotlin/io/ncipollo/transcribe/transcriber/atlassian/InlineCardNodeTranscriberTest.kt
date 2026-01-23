package io.ncipollo.transcribe.transcriber.atlassian

import io.ncipollo.transcribe.context.ADFTranscriberContext
import io.ncipollo.transcribe.data.atlassian.adf.InlineCardAttrs
import io.ncipollo.transcribe.data.atlassian.adf.InlineCardNode
import kotlin.test.Test
import kotlin.test.assertEquals

class InlineCardNodeTranscriberTest {
    private val transcriber = InlineCardNodeTranscriber()
    private val context = ADFTranscriberContext()

    @Test
    fun transcribe_withUrl() {
        val node = InlineCardNode(attrs = InlineCardAttrs(url = "https://example.com"))
        val result = transcriber.transcribe(node, context)
        assertEquals("[https://example.com](https://example.com)", result.content)
    }

    @Test
    fun transcribe_withoutUrl() {
        val node = InlineCardNode(attrs = InlineCardAttrs())
        val result = transcriber.transcribe(node, context)
        assertEquals("", result.content)
    }

    @Test
    fun transcribe_emptyUrl() {
        val node = InlineCardNode(attrs = InlineCardAttrs(url = ""))
        val result = transcriber.transcribe(node, context)
        assertEquals("", result.content)
    }
}

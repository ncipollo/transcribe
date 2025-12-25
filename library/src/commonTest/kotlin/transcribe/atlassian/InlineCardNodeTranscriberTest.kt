package transcribe.atlassian

import data.atlassian.adf.InlineCardAttrs
import data.atlassian.adf.InlineCardNode
import kotlin.test.Test
import kotlin.test.assertEquals

class InlineCardNodeTranscriberTest {
    private val transcriber = InlineCardNodeTranscriber()

    @Test
    fun transcribe_withUrl() {
        val node = InlineCardNode(attrs = InlineCardAttrs(url = "https://example.com"))
        val result = transcriber.transcribe(node)
        assertEquals("[https://example.com](https://example.com)", result.content)
    }

    @Test
    fun transcribe_withoutUrl() {
        val node = InlineCardNode(attrs = InlineCardAttrs())
        val result = transcriber.transcribe(node)
        assertEquals("", result.content)
    }

    @Test
    fun transcribe_emptyUrl() {
        val node = InlineCardNode(attrs = InlineCardAttrs(url = ""))
        val result = transcriber.transcribe(node)
        assertEquals("", result.content)
    }
}

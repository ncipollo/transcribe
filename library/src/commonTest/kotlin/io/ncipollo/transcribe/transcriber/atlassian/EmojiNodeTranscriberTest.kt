package io.ncipollo.transcribe.transcriber.atlassian

import io.ncipollo.transcribe.context.ADFTranscriberContext
import io.ncipollo.transcribe.data.atlassian.adf.EmojiAttrs
import io.ncipollo.transcribe.data.atlassian.adf.EmojiNode
import kotlin.test.Test
import kotlin.test.assertEquals

class EmojiNodeTranscriberTest {
    private val transcriber = EmojiNodeTranscriber()
    private val context = ADFTranscriberContext()

    @Test
    fun transcribe_withShortName() {
        val node = EmojiNode(attrs = EmojiAttrs(shortName = "smile"))
        val result = transcriber.transcribe(node, context)
        assertEquals(":smile:", result.content)
    }

    @Test
    fun transcribe_withText() {
        val node = EmojiNode(attrs = EmojiAttrs(shortName = "smile", text = "😊"))
        val result = transcriber.transcribe(node, context)
        assertEquals("😊", result.content)
    }

    @Test
    fun transcribe_emptyText() {
        val node = EmojiNode(attrs = EmojiAttrs(shortName = "smile", text = ""))
        val result = transcriber.transcribe(node, context)
        assertEquals(":smile:", result.content)
    }
}

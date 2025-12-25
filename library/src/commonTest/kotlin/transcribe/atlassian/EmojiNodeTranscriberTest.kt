package transcribe.atlassian

import data.atlassian.adf.EmojiAttrs
import data.atlassian.adf.EmojiNode
import kotlin.test.Test
import kotlin.test.assertEquals

class EmojiNodeTranscriberTest {
    private val transcriber = EmojiNodeTranscriber()

    @Test
    fun transcribe_withShortName() {
        val node = EmojiNode(attrs = EmojiAttrs(shortName = "smile"))
        val result = transcriber.transcribe(node)
        assertEquals(":smile:", result.content)
    }

    @Test
    fun transcribe_withText() {
        val node = EmojiNode(attrs = EmojiAttrs(shortName = "smile", text = "😊"))
        val result = transcriber.transcribe(node)
        assertEquals("😊", result.content)
    }

    @Test
    fun transcribe_emptyText() {
        val node = EmojiNode(attrs = EmojiAttrs(shortName = "smile", text = ""))
        val result = transcriber.transcribe(node)
        assertEquals(":smile:", result.content)
    }
}

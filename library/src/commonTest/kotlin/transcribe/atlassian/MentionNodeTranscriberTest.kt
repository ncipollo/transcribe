package transcribe.atlassian

import data.atlassian.adf.MentionAttrs
import data.atlassian.adf.MentionNode
import kotlin.test.Test
import kotlin.test.assertEquals

class MentionNodeTranscriberTest {

    private val transcriber = MentionNodeTranscriber()

    @Test
    fun transcribe_withText() {
        val node = MentionNode(attrs = MentionAttrs(id = "123", text = "john.doe"))
        val result = transcriber.transcribe(node)
        assertEquals("@john.doe", result.content)
    }

    @Test
    fun transcribe_withoutText() {
        val node = MentionNode(attrs = MentionAttrs(id = "123"))
        val result = transcriber.transcribe(node)
        assertEquals("@123", result.content)
    }
}


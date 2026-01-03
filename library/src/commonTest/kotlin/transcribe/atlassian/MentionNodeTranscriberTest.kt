package transcribe.atlassian

import context.ADFTranscriberContext
import data.atlassian.adf.MentionAttrs
import data.atlassian.adf.MentionNode
import kotlin.test.Test
import kotlin.test.assertEquals

class MentionNodeTranscriberTest {
    private val transcriber = MentionNodeTranscriber()
    private val context = ADFTranscriberContext()

    @Test
    fun transcribe_withText() {
        val node = MentionNode(attrs = MentionAttrs(id = "123", text = "john.doe"))
        val result = transcriber.transcribe(node, context)
        assertEquals("@john.doe", result.content)
    }

    @Test
    fun transcribe_withoutText() {
        val node = MentionNode(attrs = MentionAttrs(id = "123"))
        val result = transcriber.transcribe(node, context)
        assertEquals("@123", result.content)
    }
}

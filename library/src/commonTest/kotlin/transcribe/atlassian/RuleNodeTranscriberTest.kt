package transcribe.atlassian

import context.ADFTranscriberContext
import data.atlassian.adf.RuleNode
import kotlin.test.Test
import kotlin.test.assertEquals

class RuleNodeTranscriberTest {
    private val transcriber = RuleNodeTranscriber()
    private val context = ADFTranscriberContext()

    @Test
    fun transcribe_basic() {
        val node = RuleNode()
        val result = transcriber.transcribe(node, context)
        assertEquals("---\n\n", result.content)
    }
}

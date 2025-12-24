package transcribe.atlassian

import data.atlassian.adf.RuleNode
import kotlin.test.Test
import kotlin.test.assertEquals

class RuleNodeTranscriberTest {

    private val transcriber = RuleNodeTranscriber()

    @Test
    fun transcribe_basic() {
        val node = RuleNode()
        val result = transcriber.transcribe(node)
        assertEquals("---\n\n", result.content)
    }
}


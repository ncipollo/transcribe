package transcribe.atlassian

import data.atlassian.adf.HardBreakNode
import kotlin.test.Test
import kotlin.test.assertEquals

class HardBreakNodeTranscriberTest {

    private val transcriber = HardBreakNodeTranscriber()

    @Test
    fun transcribe_basic() {
        val node = HardBreakNode()
        val result = transcriber.transcribe(node)
        assertEquals("\n", result.content)
    }

    @Test
    fun transcribe_withAttrs() {
        val node = HardBreakNode(attrs = data.atlassian.adf.HardBreakAttrs(text = "\n"))
        val result = transcriber.transcribe(node)
        assertEquals("\n", result.content)
    }
}


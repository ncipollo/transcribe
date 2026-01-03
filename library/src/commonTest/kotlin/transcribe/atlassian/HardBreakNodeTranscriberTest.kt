package transcribe.atlassian

import context.ADFTranscriberContext
import data.atlassian.adf.HardBreakNode
import kotlin.test.Test
import kotlin.test.assertEquals

class HardBreakNodeTranscriberTest {
    private val transcriber = HardBreakNodeTranscriber()
    private val context = ADFTranscriberContext()

    @Test
    fun transcribe_basic() {
        val node = HardBreakNode()
        val result = transcriber.transcribe(node, context)
        assertEquals("\n", result.content)
    }

    @Test
    fun transcribe_withAttrs() {
        val node = HardBreakNode(attrs = data.atlassian.adf.HardBreakAttrs(text = "\n"))
        val result = transcriber.transcribe(node, context)
        assertEquals("\n", result.content)
    }
}

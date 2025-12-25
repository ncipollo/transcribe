package transcribe.atlassian

import data.atlassian.adf.TextNode
import kotlin.test.Test
import kotlin.test.assertNotNull

class DefaultADFNodeMapTest {
    @Test
    fun defaultADFNodeMapper_createsTranscriberForTextNode() {
        val mapper = defaultADFNodeMapper()
        val textNode = TextNode(text = "test")
        val transcriber = mapper.transcriberFor(textNode)
        assertNotNull(transcriber)
    }
}

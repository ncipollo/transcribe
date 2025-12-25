package transcribe.atlassian

import data.atlassian.adf.DocNode
import data.atlassian.adf.ParagraphNode
import data.atlassian.adf.TextNode
import kotlin.test.Test
import kotlin.test.assertEquals

class ADFDocumentTranscriberTest {
    private val transcriber = ADFDocumentTranscriber(defaultADFNodeMapper())
    private val context = ADFTranscriberContext()

    @Test
    fun transcribe_withContent() {
        val node =
            DocNode(
                content =
                    listOf(
                        ParagraphNode(content = listOf(TextNode(text = "Hello"))),
                        ParagraphNode(content = listOf(TextNode(text = "World"))),
                    ),
            )
        val result = transcriber.transcribe(node, context)
        assertEquals("Hello\nWorld\n", result.content)
    }

    @Test
    fun transcribe_emptyContent() {
        val node = DocNode(content = emptyList())
        val result = transcriber.transcribe(node, context)
        assertEquals("", result.content)
    }
}

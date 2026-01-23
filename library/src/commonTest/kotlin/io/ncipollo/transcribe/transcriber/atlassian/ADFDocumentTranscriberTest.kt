package io.ncipollo.transcribe.transcriber.atlassian

import io.ncipollo.transcribe.context.ADFTranscriberContext
import io.ncipollo.transcribe.data.atlassian.adf.DocNode
import io.ncipollo.transcribe.data.atlassian.adf.ParagraphNode
import io.ncipollo.transcribe.data.atlassian.adf.TextNode
import io.ncipollo.transcribe.transcriber.TranscribeResult
import io.ncipollo.transcribe.transcriber.action.AttachmentDownload
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
        val expected = TranscribeResult("Hello\nWorld\n")
        assertEquals(expected, result)
    }

    @Test
    fun transcribe_emptyContent() {
        val node = DocNode(content = emptyList())
        val result = transcriber.transcribe(node, context)
        val expected = TranscribeResult("")
        assertEquals(expected, result)
    }

    @Test
    fun transcribe_aggregatesActionsFromChildren() {
        val action1 = AttachmentDownload(downloadPath = "/path/to/file1.pdf")
        val action2 = AttachmentDownload(downloadPath = "/path/to/file2.jpg")
        val customParagraphTranscriber = TestParagraphTranscriber(listOf(action1, action2))
        val defaultMapper = defaultADFNodeMapper()
        val customMapper =
            defaultMapper + ADFNodeMapper(
                mapOf(
                    ParagraphNode::class to { customParagraphTranscriber },
                ),
            )
        val customDocumentTranscriber = ADFDocumentTranscriber(customMapper)

        val node =
            DocNode(
                content =
                listOf(
                    ParagraphNode(content = listOf(TextNode(text = "Hello"))),
                    ParagraphNode(content = listOf(TextNode(text = "World"))),
                ),
            )
        val result = customDocumentTranscriber.transcribe(node, context)
        val expectedActions = listOf(action1, action2, action1, action2)
        assertEquals(expectedActions, result.actions)
    }

    private class TestParagraphTranscriber(
        private val actions: List<AttachmentDownload>,
    ) : ADFTranscriber<ParagraphNode> {
        override fun transcribe(
            input: ParagraphNode,
            context: ADFTranscriberContext,
        ): TranscribeResult<String> = TranscribeResult("test\n", actions)
    }
}

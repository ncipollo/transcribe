package io.ncipollo.transcribe.transcriber.atlassian

import io.ncipollo.transcribe.context.ADFTranscriberContext
import io.ncipollo.transcribe.data.atlassian.adf.HeadingAttrs
import io.ncipollo.transcribe.data.atlassian.adf.HeadingNode
import io.ncipollo.transcribe.data.atlassian.adf.TextNode
import kotlin.test.Test
import kotlin.test.assertEquals

class HeadingNodeTranscriberTest {
    private val transcriber = HeadingNodeTranscriber(defaultADFNodeMapper())
    private val context = ADFTranscriberContext()

    @Test
    fun transcribe_level1() {
        val node =
            HeadingNode(
                attrs = HeadingAttrs(level = 1),
                content = listOf(TextNode(text = "Title")),
            )
        val result = transcriber.transcribe(node, context)
        assertEquals("\n# Title\n", result.content)
    }

    @Test
    fun transcribe_level3() {
        val node =
            HeadingNode(
                attrs = HeadingAttrs(level = 3),
                content = listOf(TextNode(text = "Subtitle")),
            )
        val result = transcriber.transcribe(node, context)
        assertEquals("\n### Subtitle\n", result.content)
    }

    @Test
    fun transcribe_level6() {
        val node =
            HeadingNode(
                attrs = HeadingAttrs(level = 6),
                content = listOf(TextNode(text = "Smallest")),
            )
        val result = transcriber.transcribe(node, context)
        assertEquals("\n###### Smallest\n", result.content)
    }

    @Test
    fun transcribe_emptyContent() {
        val node =
            HeadingNode(
                attrs = HeadingAttrs(level = 2),
                content = null,
            )
        val result = transcriber.transcribe(node, context)
        assertEquals("\n## \n", result.content)
    }
}

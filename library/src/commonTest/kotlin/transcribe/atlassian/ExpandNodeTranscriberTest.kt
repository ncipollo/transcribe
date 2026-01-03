package transcribe.atlassian

import context.ADFTranscriberContext
import data.atlassian.adf.ExpandAttrs
import data.atlassian.adf.ExpandNode
import data.atlassian.adf.ParagraphNode
import data.atlassian.adf.TextNode
import kotlin.test.Test
import kotlin.test.assertEquals

class ExpandNodeTranscriberTest {
    private val transcriber = ExpandNodeTranscriber(defaultADFNodeMapper())
    private val context = ADFTranscriberContext()

    @Test
    fun transcribe_withTitleAndContent() {
        val node =
            ExpandNode(
                attrs = ExpandAttrs(title = "Collapse"),
                content =
                listOf(
                    ParagraphNode(content = listOf(TextNode(text = "Content which is kinda hidden."))),
                ),
            )
        val result = transcriber.transcribe(node, context)
        assertEquals("<details>\n<summary>Collapse</summary>\nContent which is kinda hidden.\n</details>\n", result.content)
    }

    @Test
    fun transcribe_withoutTitle() {
        val node =
            ExpandNode(
                attrs = null,
                content =
                listOf(
                    ParagraphNode(content = listOf(TextNode(text = "Hidden content"))),
                ),
            )
        val result = transcriber.transcribe(node, context)
        assertEquals("<details>\n<summary>Click to expand</summary>\nHidden content\n</details>\n", result.content)
    }

    @Test
    fun transcribe_emptyContent() {
        val node =
            ExpandNode(
                attrs = ExpandAttrs(title = "Empty expand"),
                content = emptyList(),
            )
        val result = transcriber.transcribe(node, context)
        assertEquals("<details>\n<summary>Empty expand</summary>\n</details>\n", result.content)
    }

    @Test
    fun transcribe_multipleBlocks() {
        val node =
            ExpandNode(
                attrs = ExpandAttrs(title = "Multiple items"),
                content =
                listOf(
                    ParagraphNode(content = listOf(TextNode(text = "First paragraph"))),
                    ParagraphNode(content = listOf(TextNode(text = "Second paragraph"))),
                ),
            )
        val result = transcriber.transcribe(node, context)
        assertEquals("<details>\n<summary>Multiple items</summary>\nFirst paragraph\nSecond paragraph\n</details>\n", result.content)
    }
}

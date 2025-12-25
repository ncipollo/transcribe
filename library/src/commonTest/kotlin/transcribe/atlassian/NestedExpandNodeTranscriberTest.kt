package transcribe.atlassian

import data.atlassian.adf.NestedExpandAttrs
import data.atlassian.adf.NestedExpandNode
import data.atlassian.adf.ParagraphNode
import data.atlassian.adf.TextNode
import kotlin.test.Test
import kotlin.test.assertEquals

class NestedExpandNodeTranscriberTest {
    private val transcriber = NestedExpandNodeTranscriber(defaultADFNodeMapper())

    @Test
    fun transcribe_withTitleAndContent() {
        val node =
            NestedExpandNode(
                attrs = NestedExpandAttrs(title = "Nested Collapse"),
                content =
                    listOf(
                        ParagraphNode(content = listOf(TextNode(text = "Nested content which is hidden."))),
                    ),
            )
        val result = transcriber.transcribe(node)
        assertEquals("<details>\n<summary>Nested Collapse</summary>\nNested content which is hidden.\n</details>\n", result.content)
    }

    @Test
    fun transcribe_withoutTitle() {
        val node =
            NestedExpandNode(
                attrs = NestedExpandAttrs(title = null),
                content =
                    listOf(
                        ParagraphNode(content = listOf(TextNode(text = "Hidden nested content"))),
                    ),
            )
        val result = transcriber.transcribe(node)
        assertEquals("<details>\n<summary>Click to expand</summary>\nHidden nested content\n</details>\n", result.content)
    }

    @Test
    fun transcribe_emptyContent() {
        val node =
            NestedExpandNode(
                attrs = NestedExpandAttrs(title = "Empty nested expand"),
                content = emptyList(),
            )
        val result = transcriber.transcribe(node)
        assertEquals("<details>\n<summary>Empty nested expand</summary>\n</details>\n", result.content)
    }

    @Test
    fun transcribe_multipleBlocks() {
        val node =
            NestedExpandNode(
                attrs = NestedExpandAttrs(title = "Multiple nested items"),
                content =
                    listOf(
                        ParagraphNode(content = listOf(TextNode(text = "First nested paragraph"))),
                        ParagraphNode(content = listOf(TextNode(text = "Second nested paragraph"))),
                    ),
            )
        val result = transcriber.transcribe(node)
        assertEquals("<details>\n<summary>Multiple nested items</summary>\nFirst nested paragraph\nSecond nested paragraph\n</details>\n", result.content)
    }
}


package transcribe.atlassian

import context.ADFTranscriberContext
import data.atlassian.adf.ListItemNode
import data.atlassian.adf.OrderedListAttrs
import data.atlassian.adf.OrderedListNode
import data.atlassian.adf.ParagraphNode
import data.atlassian.adf.TextNode
import kotlin.test.Test
import kotlin.test.assertEquals

class OrderedListNodeTranscriberTest {
    private val transcriber = OrderedListNodeTranscriber(defaultADFNodeMapper())
    private val context = ADFTranscriberContext()

    @Test
    fun transcribe_withItems() {
        val node =
            OrderedListNode(
                content =
                listOf(
                    ListItemNode(
                        content =
                        listOf(
                            ParagraphNode(content = listOf(TextNode(text = "First"))),
                        ),
                    ),
                    ListItemNode(
                        content =
                        listOf(
                            ParagraphNode(content = listOf(TextNode(text = "Second"))),
                        ),
                    ),
                ),
            )
        val result = transcriber.transcribe(node, context)
        assertEquals("1. First\n2. Second\n", result.content)
    }

    @Test
    fun transcribe_withStartOrder() {
        val node =
            OrderedListNode(
                attrs = OrderedListAttrs(order = 5),
                content =
                listOf(
                    ListItemNode(
                        content =
                        listOf(
                            ParagraphNode(content = listOf(TextNode(text = "Item"))),
                        ),
                    ),
                ),
            )
        val result = transcriber.transcribe(node, context)
        assertEquals("5. Item\n", result.content)
    }

    @Test
    fun transcribe_emptyList() {
        val node = OrderedListNode(content = emptyList())
        val result = transcriber.transcribe(node, context)
        assertEquals("", result.content)
    }
}

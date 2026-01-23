package io.ncipollo.transcribe.transcriber.atlassian

import io.ncipollo.transcribe.context.ADFTranscriberContext
import io.ncipollo.transcribe.data.atlassian.adf.BulletListNode
import io.ncipollo.transcribe.data.atlassian.adf.ListItemNode
import io.ncipollo.transcribe.data.atlassian.adf.OrderedListAttrs
import io.ncipollo.transcribe.data.atlassian.adf.OrderedListNode
import io.ncipollo.transcribe.data.atlassian.adf.ParagraphNode
import io.ncipollo.transcribe.data.atlassian.adf.TextNode
import io.ncipollo.transcribe.transcriber.TranscribeResult
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
        val expected = TranscribeResult("1. First\n2. Second\n")
        assertEquals(expected, result)
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
        val expected = TranscribeResult("5. Item\n")
        assertEquals(expected, result)
    }

    @Test
    fun transcribe_emptyList() {
        val node = OrderedListNode(content = emptyList())
        val result = transcriber.transcribe(node, context)
        val expected = TranscribeResult("")
        assertEquals(expected, result)
    }

    @Test
    fun transcribe_withNestedOrderedList() {
        val node =
            OrderedListNode(
                content =
                listOf(
                    ListItemNode(
                        content =
                        listOf(
                            ParagraphNode(content = listOf(TextNode(text = "First"))),
                            OrderedListNode(
                                content =
                                listOf(
                                    ListItemNode(
                                        content =
                                        listOf(
                                            ParagraphNode(content = listOf(TextNode(text = "Sub-first"))),
                                        ),
                                    ),
                                ),
                            ),
                        ),
                    ),
                ),
            )
        val result = transcriber.transcribe(node, context)

        val expected =
            TranscribeResult(
                "1. First\n" +
                    "    1. Sub-first\n",
            )
        assertEquals(expected, result)
    }

    @Test
    fun transcribe_withMixedNestedLists() {
        val node =
            OrderedListNode(
                content =
                listOf(
                    ListItemNode(
                        content =
                        listOf(
                            ParagraphNode(content = listOf(TextNode(text = "Ordered item"))),
                            BulletListNode(
                                content =
                                listOf(
                                    ListItemNode(
                                        content =
                                        listOf(
                                            ParagraphNode(content = listOf(TextNode(text = "Bullet subitem"))),
                                        ),
                                    ),
                                ),
                            ),
                        ),
                    ),
                ),
            )
        val result = transcriber.transcribe(node, context)

        val expected =
            TranscribeResult(
                "1. Ordered item\n" +
                    "    - Bullet subitem\n",
            )
        assertEquals(expected, result)
    }
}

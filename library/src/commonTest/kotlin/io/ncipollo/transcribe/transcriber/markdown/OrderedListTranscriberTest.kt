package io.ncipollo.transcribe.transcriber.markdown

import io.ncipollo.transcribe.context.MarkdownContext
import io.ncipollo.transcribe.data.atlassian.adf.BulletListNode
import io.ncipollo.transcribe.data.atlassian.adf.ListItemNode
import io.ncipollo.transcribe.data.atlassian.adf.OrderedListNode
import io.ncipollo.transcribe.data.atlassian.adf.ParagraphNode
import io.ncipollo.transcribe.data.atlassian.adf.TextNode
import org.intellij.markdown.MarkdownElementTypes
import kotlin.test.Test
import kotlin.test.assertEquals

class OrderedListTranscriberTest {
    private val nodeMapper = defaultMarkdownNodeMapper()
    private val transcriber = OrderedListTranscriber(nodeMapper)

    @Test
    fun transcribe_orderedList() {
        val markdown = "1. Item 1\n2. Item 2"
        val listNode = MarkdownTestHelper.findNode(markdown, MarkdownElementTypes.ORDERED_LIST)
        val context = MarkdownContext(markdownText = markdown)
        val result = transcriber.transcribe(listNode, context)

        val expected =
            OrderedListNode(
                content =
                listOf(
                    ListItemNode(
                        content =
                        listOf(
                            ParagraphNode(
                                content =
                                listOf(
                                    TextNode(text = "Item 1"),
                                ),
                            ),
                        ),
                    ),
                    ListItemNode(
                        content =
                        listOf(
                            ParagraphNode(
                                content =
                                listOf(
                                    TextNode(text = "Item 2"),
                                ),
                            ),
                        ),
                    ),
                ),
            )
        assertEquals(expected, result.content)
    }

    @Test
    fun transcribe_orderedListWithSubitems() {
        val markdown = "1. Item 1\n   1. Subitem 1a\n   2. Subitem 1b\n2. Item 2"
        val listNode = MarkdownTestHelper.findNode(markdown, MarkdownElementTypes.ORDERED_LIST)
        val context = MarkdownContext(markdownText = markdown)
        val result = transcriber.transcribe(listNode, context)

        val expected =
            OrderedListNode(
                content =
                listOf(
                    ListItemNode(
                        content =
                        listOf(
                            ParagraphNode(
                                content =
                                listOf(
                                    TextNode(text = "Item 1"),
                                ),
                            ),
                            OrderedListNode(
                                content =
                                listOf(
                                    ListItemNode(
                                        content =
                                        listOf(
                                            ParagraphNode(
                                                content =
                                                listOf(
                                                    TextNode(text = "Subitem 1a"),
                                                ),
                                            ),
                                        ),
                                    ),
                                    ListItemNode(
                                        content =
                                        listOf(
                                            ParagraphNode(
                                                content =
                                                listOf(
                                                    TextNode(text = "Subitem 1b"),
                                                ),
                                            ),
                                        ),
                                    ),
                                ),
                            ),
                        ),
                    ),
                    ListItemNode(
                        content =
                        listOf(
                            ParagraphNode(
                                content =
                                listOf(
                                    TextNode(text = "Item 2"),
                                ),
                            ),
                        ),
                    ),
                ),
            )
        assertEquals(expected, result.content)
    }

    @Test
    fun transcribe_orderedListWithNestedBulletList() {
        val markdown = "1. Ordered item\n   - Bullet subitem"
        val listNode = MarkdownTestHelper.findNode(markdown, MarkdownElementTypes.ORDERED_LIST)
        val context = MarkdownContext(markdownText = markdown)
        val result = transcriber.transcribe(listNode, context)

        val expected =
            OrderedListNode(
                content =
                listOf(
                    ListItemNode(
                        content =
                        listOf(
                            ParagraphNode(
                                content =
                                listOf(
                                    TextNode(text = "Ordered item"),
                                ),
                            ),
                            BulletListNode(
                                content =
                                listOf(
                                    ListItemNode(
                                        content =
                                        listOf(
                                            ParagraphNode(
                                                content =
                                                listOf(
                                                    TextNode(text = "Bullet subitem"),
                                                ),
                                            ),
                                        ),
                                    ),
                                ),
                            ),
                        ),
                    ),
                ),
            )
        assertEquals(expected, result.content)
    }
}

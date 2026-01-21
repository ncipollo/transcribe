package transcribe.markdown

import context.MarkdownContext
import data.atlassian.adf.BulletListNode
import data.atlassian.adf.ListItemNode
import data.atlassian.adf.OrderedListNode
import data.atlassian.adf.ParagraphNode
import data.atlassian.adf.TextNode
import org.intellij.markdown.MarkdownElementTypes
import kotlin.test.Test
import kotlin.test.assertEquals

class BulletListTranscriberTest {
    private val nodeMapper = defaultMarkdownNodeMapper()
    private val transcriber = BulletListTranscriber(nodeMapper)

    @Test
    fun transcribe_bulletList() {
        val markdown = "- Item 1\n- Item 2"
        val listNode = MarkdownTestHelper.findNode(markdown, MarkdownElementTypes.UNORDERED_LIST)
        val context = MarkdownContext(markdownText = markdown)
        val result = transcriber.transcribe(listNode, context)

        val expected =
            BulletListNode(
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
    fun transcribe_bulletListWithSubbullets() {
        val markdown = "- Item 1\n  - Subitem 1a\n  - Subitem 1b\n- Item 2"
        val listNode = MarkdownTestHelper.findNode(markdown, MarkdownElementTypes.UNORDERED_LIST)
        val context = MarkdownContext(markdownText = markdown)
        val result = transcriber.transcribe(listNode, context)

        val expected =
            BulletListNode(
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
                            BulletListNode(
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
    fun transcribe_bulletListWithDeepNesting() {
        val markdown = "- Level 1\n  - Level 2\n    - Level 3"
        val listNode = MarkdownTestHelper.findNode(markdown, MarkdownElementTypes.UNORDERED_LIST)
        val context = MarkdownContext(markdownText = markdown)
        val result = transcriber.transcribe(listNode, context)

        val expected =
            BulletListNode(
                content =
                listOf(
                    ListItemNode(
                        content =
                        listOf(
                            ParagraphNode(
                                content =
                                listOf(
                                    TextNode(text = "Level 1"),
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
                                                    TextNode(text = "Level 2"),
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
                                                                    TextNode(text = "Level 3"),
                                                                ),
                                                            ),
                                                        ),
                                                    ),
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

    @Test
    fun transcribe_bulletListWithMixedNestedTypes() {
        val markdown = "- Bullet item\n  1. Ordered subitem\n  2. Another ordered"
        val listNode = MarkdownTestHelper.findNode(markdown, MarkdownElementTypes.UNORDERED_LIST)
        val context = MarkdownContext(markdownText = markdown)
        val result = transcriber.transcribe(listNode, context)

        val expected =
            BulletListNode(
                content =
                listOf(
                    ListItemNode(
                        content =
                        listOf(
                            ParagraphNode(
                                content =
                                listOf(
                                    TextNode(text = "Bullet item"),
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
                                                    TextNode(text = "Ordered subitem"),
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
                                                    TextNode(text = "Another ordered"),
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

package transcribe.atlassian

import context.ADFTranscriberContext
import data.atlassian.adf.BulletListNode
import data.atlassian.adf.ListItemNode
import data.atlassian.adf.ParagraphNode
import data.atlassian.adf.TextNode
import transcribe.TranscribeResult
import kotlin.test.Test
import kotlin.test.assertEquals

class BulletListNodeTranscriberTest {
    private val transcriber = BulletListNodeTranscriber(defaultADFNodeMapper())
    private val context = ADFTranscriberContext()

    @Test
    fun transcribe_withItems() {
        val node =
            BulletListNode(
                content =
                listOf(
                    ListItemNode(
                        content =
                        listOf(
                            ParagraphNode(content = listOf(TextNode(text = "First item"))),
                        ),
                    ),
                    ListItemNode(
                        content =
                        listOf(
                            ParagraphNode(content = listOf(TextNode(text = "Second item"))),
                        ),
                    ),
                ),
            )
        val result = transcriber.transcribe(node, context)
        val expected = TranscribeResult("- First item\n- Second item\n")
        assertEquals(expected, result)
    }

    @Test
    fun transcribe_emptyList() {
        val node = BulletListNode(content = emptyList())
        val result = transcriber.transcribe(node, context)
        val expected = TranscribeResult("")
        assertEquals(expected, result)
    }

    @Test
    fun transcribe_withNestedList() {
        val node =
            BulletListNode(
                content =
                listOf(
                    ListItemNode(
                        content =
                        listOf(
                            ParagraphNode(content = listOf(TextNode(text = "Item 1"))),
                            BulletListNode(
                                content =
                                listOf(
                                    ListItemNode(
                                        content =
                                        listOf(
                                            ParagraphNode(content = listOf(TextNode(text = "Subitem 1a"))),
                                        ),
                                    ),
                                    ListItemNode(
                                        content =
                                        listOf(
                                            ParagraphNode(content = listOf(TextNode(text = "Subitem 1b"))),
                                        ),
                                    ),
                                ),
                            ),
                        ),
                    ),
                    ListItemNode(
                        content =
                        listOf(
                            ParagraphNode(content = listOf(TextNode(text = "Item 2"))),
                        ),
                    ),
                ),
            )
        val result = transcriber.transcribe(node, context)

        val expected =
            TranscribeResult(
                "- Item 1\n" +
                    "    - Subitem 1a\n" +
                    "    - Subitem 1b\n" +
                    "- Item 2\n",
            )
        assertEquals(expected, result)
    }

    @Test
    fun transcribe_withDeeplyNestedLists() {
        val node =
            BulletListNode(
                content =
                listOf(
                    ListItemNode(
                        content =
                        listOf(
                            ParagraphNode(content = listOf(TextNode(text = "Level 0"))),
                            BulletListNode(
                                content =
                                listOf(
                                    ListItemNode(
                                        content =
                                        listOf(
                                            ParagraphNode(content = listOf(TextNode(text = "Level 1"))),
                                            BulletListNode(
                                                content =
                                                listOf(
                                                    ListItemNode(
                                                        content =
                                                        listOf(
                                                            ParagraphNode(content = listOf(TextNode(text = "Level 2"))),
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
        val result = transcriber.transcribe(node, context)

        val expected =
            TranscribeResult(
                "- Level 0\n" +
                    "    - Level 1\n" +
                    "        - Level 2\n",
            )
        assertEquals(expected, result)
    }
}

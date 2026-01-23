package io.ncipollo.transcribe.transcriber.markdown

import io.ncipollo.transcribe.data.atlassian.adf.BulletListNode
import io.ncipollo.transcribe.data.atlassian.adf.HardBreakNode
import io.ncipollo.transcribe.data.atlassian.adf.ListItemNode
import io.ncipollo.transcribe.data.atlassian.adf.ParagraphNode
import io.ncipollo.transcribe.data.atlassian.adf.TextNode
import kotlin.test.Test
import kotlin.test.assertEquals

class BlockquoteCleanupHelperTest {
    @Test
    fun cleanupBlockContent_removesEmptyMarkerParagraph() {
        val content =
            listOf(
                ParagraphNode(content = listOf(TextNode(text = "Normal text"))),
                ParagraphNode(content = listOf(TextNode(text = ">"))),
                ParagraphNode(content = listOf(TextNode(text = "More text"))),
            )
        val result = BlockquoteCleanupHelper.cleanupBlockContent(content)

        val expected =
            listOf(
                ParagraphNode(content = listOf(TextNode(text = "Normal text"))),
                ParagraphNode(content = listOf(TextNode(text = "More text"))),
            )
        assertEquals(expected, result)
    }

    @Test
    fun cleanupBlockContent_cleansInlineAndRemovesParagraph() {
        val content =
            listOf(
                ParagraphNode(
                    content =
                    listOf(
                        TextNode(text = "Text"),
                        HardBreakNode(),
                        TextNode(text = " "),
                        TextNode(text = "More"),
                    ),
                ),
                ParagraphNode(content = listOf(TextNode(text = "> "))),
            )
        val result = BlockquoteCleanupHelper.cleanupBlockContent(content)

        val expected =
            listOf(
                ParagraphNode(
                    content =
                    listOf(
                        TextNode(text = "Text"),
                        HardBreakNode(),
                        TextNode(text = "More"),
                    ),
                ),
            )
        assertEquals(expected, result)
    }

    @Test
    fun cleanupBlockContent_preservesNonParagraphNodes() {
        val content =
            listOf(
                ParagraphNode(content = listOf(TextNode(text = "Text"))),
                BulletListNode(
                    content =
                    listOf(
                        ListItemNode(
                            content =
                            listOf(
                                ParagraphNode(content = listOf(TextNode(text = "Item"))),
                            ),
                        ),
                    ),
                ),
                ParagraphNode(content = listOf(TextNode(text = ">"))),
            )
        val result = BlockquoteCleanupHelper.cleanupBlockContent(content)

        val expected =
            listOf(
                ParagraphNode(content = listOf(TextNode(text = "Text"))),
                BulletListNode(
                    content =
                    listOf(
                        ListItemNode(
                            content =
                            listOf(
                                ParagraphNode(content = listOf(TextNode(text = "Item"))),
                            ),
                        ),
                    ),
                ),
            )
        assertEquals(expected, result)
    }
}

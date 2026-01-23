package io.ncipollo.transcribe.transcriber.markdown

import io.ncipollo.transcribe.context.MarkdownContext
import io.ncipollo.transcribe.data.atlassian.adf.BlockquoteNode
import io.ncipollo.transcribe.data.atlassian.adf.BulletListNode
import io.ncipollo.transcribe.data.atlassian.adf.EmMark
import io.ncipollo.transcribe.data.atlassian.adf.HardBreakNode
import io.ncipollo.transcribe.data.atlassian.adf.ListItemNode
import io.ncipollo.transcribe.data.atlassian.adf.ParagraphNode
import io.ncipollo.transcribe.data.atlassian.adf.StrongMark
import io.ncipollo.transcribe.data.atlassian.adf.TextNode
import org.intellij.markdown.MarkdownElementTypes
import kotlin.test.Test
import kotlin.test.assertEquals

class BlockquoteTranscriberTest {
    private val nodeMapper = defaultMarkdownNodeMapper()
    private val transcriber = BlockquoteTranscriber(nodeMapper)

    @Test
    fun transcribe_blockquote() {
        val markdown = "> Quote text"
        val blockquoteNode = MarkdownTestHelper.findNode(markdown, MarkdownElementTypes.BLOCK_QUOTE)
        val context = MarkdownContext(markdownText = markdown)
        val result = transcriber.transcribe(blockquoteNode, context)

        val expected =
            BlockquoteNode(
                content =
                listOf(
                    ParagraphNode(
                        content =
                        listOf(
                            TextNode(text = "Quote text"),
                        ),
                    ),
                ),
            )
        assertEquals(expected, result.content)
    }

    @Test
    fun transcribe_complexBlockquote_removesJunkNodes() {
        val markdown =
            """
            |> **This** and *that.*
            |> Next line.
            |> - Bullet 1
            |> - Bullet 2
            """.trimMargin()
        val blockquoteNode = MarkdownTestHelper.findNode(markdown, MarkdownElementTypes.BLOCK_QUOTE)
        val context = MarkdownContext(markdownText = markdown)
        val result = transcriber.transcribe(blockquoteNode, context)

        val expected =
            BlockquoteNode(
                content =
                listOf(
                    ParagraphNode(
                        content =
                        listOf(
                            TextNode(text = "This", marks = listOf(StrongMark)),
                            TextNode(text = " "),
                            TextNode(text = "and"),
                            TextNode(text = " "),
                            TextNode(text = "that.", marks = listOf(EmMark)),
                            HardBreakNode(),
                            TextNode(text = "Next line."),
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
                                            TextNode(text = "Bullet 1"),
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
                                            TextNode(text = "Bullet 2"),
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

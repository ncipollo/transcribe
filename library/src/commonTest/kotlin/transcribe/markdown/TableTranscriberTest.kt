package transcribe.markdown

import data.atlassian.adf.EmMark
import data.atlassian.adf.ParagraphNode
import data.atlassian.adf.StrongMark
import data.atlassian.adf.TableCellNode
import data.atlassian.adf.TableHeaderNode
import data.atlassian.adf.TableNode
import data.atlassian.adf.TableRowNode
import data.atlassian.adf.TextNode
import org.intellij.markdown.flavours.gfm.GFMElementTypes
import kotlin.test.Test
import kotlin.test.assertEquals

class TableTranscriberTest {
    private val nodeMapper = defaultMarkdownNodeMapper()
    private val transcriber = TableTranscriber(nodeMapper)

    @Test
    fun transcribe_singleColumnSingleRow() {
        val markdown = "| Header |\n|--------|\n| Cell |"
        val tableNode = MarkdownTestHelper.findNode(markdown, GFMElementTypes.TABLE)
        val context = MarkdownContext(markdownText = markdown)
        val result = transcriber.transcribe(tableNode, context)

        val expected =
            TableNode(
                content =
                    listOf(
                        TableRowNode(
                            content =
                                listOf(
                                    TableHeaderNode(
                                        content =
                                            listOf(
                                                ParagraphNode(
                                                    content =
                                                        listOf(
                                                            TextNode(text = "Header"),
                                                        ),
                                                ),
                                            ),
                                    ),
                                ),
                        ),
                        TableRowNode(
                            content =
                                listOf(
                                    TableCellNode(
                                        content =
                                            listOf(
                                                ParagraphNode(
                                                    content =
                                                        listOf(
                                                            TextNode(text = "Cell"),
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
    fun transcribe_cellWithMixedFormatting() {
        val markdown = "| Header |\n|--------|\n| **bold** *italic* normal |"
        val tableNode = MarkdownTestHelper.findNode(markdown, GFMElementTypes.TABLE)
        val context = MarkdownContext(markdownText = markdown)
        val result = transcriber.transcribe(tableNode, context)

        val expected =
            TableNode(
                content =
                    listOf(
                        TableRowNode(
                            content =
                                listOf(
                                    TableHeaderNode(
                                        content =
                                            listOf(
                                                ParagraphNode(
                                                    content =
                                                        listOf(
                                                            TextNode(text = "Header"),
                                                        ),
                                                ),
                                            ),
                                    ),
                                ),
                        ),
                        TableRowNode(
                            content =
                                listOf(
                                    TableCellNode(
                                        content =
                                            listOf(
                                                ParagraphNode(
                                                    content =
                                                        listOf(
                                                            TextNode(text = "bold", marks = listOf(StrongMark)),
                                                            TextNode(text = " "),
                                                            TextNode(text = "italic", marks = listOf(EmMark)),
                                                            TextNode(text = " "),
                                                            TextNode(text = "normal"),
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
    fun transcribe_twoColumnsThreeRows() {
        val markdown = "| Col1 | Col2 |\n|------|------|\n| A | B |\n| C | D |"
        val tableNode = MarkdownTestHelper.findNode(markdown, GFMElementTypes.TABLE)
        val context = MarkdownContext(markdownText = markdown)
        val result = transcriber.transcribe(tableNode, context)

        val expected =
            TableNode(
                content =
                    listOf(
                        TableRowNode(
                            content =
                                listOf(
                                    TableHeaderNode(
                                        content =
                                            listOf(
                                                ParagraphNode(
                                                    content =
                                                        listOf(
                                                            TextNode(text = "Col1"),
                                                        ),
                                                ),
                                            ),
                                    ),
                                    TableHeaderNode(
                                        content =
                                            listOf(
                                                ParagraphNode(
                                                    content =
                                                        listOf(
                                                            TextNode(text = "Col2"),
                                                        ),
                                                ),
                                            ),
                                    ),
                                ),
                        ),
                        TableRowNode(
                            content =
                                listOf(
                                    TableCellNode(
                                        content =
                                            listOf(
                                                ParagraphNode(
                                                    content =
                                                        listOf(
                                                            TextNode(text = "A"),
                                                        ),
                                                ),
                                            ),
                                    ),
                                    TableCellNode(
                                        content =
                                            listOf(
                                                ParagraphNode(
                                                    content =
                                                        listOf(
                                                            TextNode(text = "B"),
                                                        ),
                                                ),
                                            ),
                                    ),
                                ),
                        ),
                        TableRowNode(
                            content =
                                listOf(
                                    TableCellNode(
                                        content =
                                            listOf(
                                                ParagraphNode(
                                                    content =
                                                        listOf(
                                                            TextNode(text = "C"),
                                                        ),
                                                ),
                                            ),
                                    ),
                                    TableCellNode(
                                        content =
                                            listOf(
                                                ParagraphNode(
                                                    content =
                                                        listOf(
                                                            TextNode(text = "D"),
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

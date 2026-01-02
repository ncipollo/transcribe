package transcribe.markdown

import data.atlassian.adf.BulletListNode
import data.atlassian.adf.ExpandAttrs
import data.atlassian.adf.ExpandNode
import data.atlassian.adf.HeadingAttrs
import data.atlassian.adf.HeadingNode
import data.atlassian.adf.ListItemNode
import data.atlassian.adf.ParagraphNode
import data.atlassian.adf.TextNode
import data.markdown.parser.printTree
import org.intellij.markdown.MarkdownElementTypes
import kotlin.test.Test
import kotlin.test.assertEquals

class DetailsTranscriberTest {
    private val nodeMapper = defaultMarkdownNodeMapper()
    private val transcriber = DetailsTranscriber(nodeMapper)

    @Test
    fun transcribe_detailsWithSimpleContent_returnsExpandNode() {
        val markdown = "<details><summary>Collapse</summary>- Content which is kinda hidden.</details>"
        val htmlBlockNode = MarkdownTestHelper.findNode(markdown, MarkdownElementTypes.HTML_BLOCK)
        val context = MarkdownContext(markdownText = markdown)
        val result = transcriber.transcribe(htmlBlockNode, context)

        val expected =
            ExpandNode(
                content =
                    listOf(
                        BulletListNode(
                            content =
                                listOf(
                                    ListItemNode(
                                        content =
                                            listOf(
                                                ParagraphNode(
                                                    content =
                                                        listOf(
                                                            TextNode(text = "Content which is kinda hidden."),
                                                        ),
                                                ),
                                            ),
                                    ),
                                ),
                        ),
                    ),
                attrs = ExpandAttrs(title = "Collapse"),
            )
        assertEquals(expected, result.content)
    }

    @Test
    fun transcribe_detailsWithMarkdownContent_returnsTranscribedContent() {
        val markdown =
            """
            <details>
            <summary>Click to expand</summary>
            # Heading
            - List item 1
            - List item 2
            </details>
            """.trimIndent()
        val htmlBlockNode = MarkdownTestHelper.findNode(markdown, MarkdownElementTypes.HTML_BLOCK)
        htmlBlockNode.parent?.printTree()
        val context = MarkdownContext(markdownText = markdown)
        val result = transcriber.transcribe(htmlBlockNode, context)

        val expected =
            ExpandNode(
                content =
                    listOf(
                        HeadingNode(
                            attrs = HeadingAttrs(level = 1),
                            content =
                                listOf(
                                    TextNode(text = "Heading"),
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
                                                            TextNode(text = "List item 1"),
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
                                                            TextNode(text = "List item 2"),
                                                        ),
                                                ),
                                            ),
                                    ),
                                ),
                        ),
                    ),
                attrs = ExpandAttrs(title = "Click to expand"),
            )
        assertEquals(expected, result.content)
    }

    @Test
    fun transcribe_detailsWithEmptyContent_returnsEmptyExpandNode() {
        val markdown = "<details><summary>Empty</summary></details>"
        val htmlBlockNode = MarkdownTestHelper.findNode(markdown, MarkdownElementTypes.HTML_BLOCK)
        val context = MarkdownContext(markdownText = markdown)
        val result = transcriber.transcribe(htmlBlockNode, context)

        val expected =
            ExpandNode(
                content = emptyList(),
                attrs = ExpandAttrs(title = "Empty"),
            )
        assertEquals(expected, result.content)
    }

    @Test
    fun transcribe_detailsWithParagraphs_returnsMultipleParagraphs() {
        // Tests handling of blank lines which cause the markdown parser to split the HTML block
        val markdown =
            """
            <details>
            <summary>Summary</summary>
            First paragraph.
            
            Second paragraph.
            </details>
            """.trimIndent()
        val htmlBlockNode = MarkdownTestHelper.findNode(markdown, MarkdownElementTypes.HTML_BLOCK)
        htmlBlockNode.parent?.printTree()
        val context = MarkdownContext(markdownText = markdown)
        val result = transcriber.transcribe(htmlBlockNode, context)

        val expected =
            ExpandNode(
                content =
                    listOf(
                        ParagraphNode(
                            content =
                                listOf(
                                    TextNode(text = "First paragraph."),
                                ),
                        ),
                        ParagraphNode(
                            content =
                                listOf(
                                    TextNode(text = "Second paragraph."),
                                ),
                        ),
                    ),
                attrs = ExpandAttrs(title = "Summary"),
            )
        assertEquals(expected, result.content)
    }
}


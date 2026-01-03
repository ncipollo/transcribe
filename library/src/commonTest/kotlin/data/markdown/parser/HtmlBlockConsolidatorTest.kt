package data.markdown.parser

import org.intellij.markdown.MarkdownElementTypes
import org.intellij.markdown.MarkdownTokenTypes
import org.intellij.markdown.ast.ASTNode
import kotlin.test.Test
import kotlin.test.assertEquals

class HtmlBlockConsolidatorTest {
    @Test
    fun consolidate_detailsWithBlankLines_consolidatesBlocks() {
        val markdown =
            """
            <details>
            <summary>Summary</summary>
            First paragraph.

            Second paragraph.
            </details>
            """.trimIndent()

        val parser = defaultMarkdownParser()
        val rootNode = parser.buildMarkdownTreeFromString(markdown)
        val consolidated = HtmlBlockConsolidator.consolidate(rootNode, markdown)

        val expected = TranscribeASTNode(
            type = MarkdownElementTypes.MARKDOWN_FILE,
            children = listOf(
                TranscribeASTNode(
                    type = MarkdownElementTypes.HTML_BLOCK,
                    children = emptyList(),
                    startOffset = 0,
                    endOffset = markdown.length,
                ),
            ),
            startOffset = 0,
            endOffset = markdown.length,
        )

        assertEquals(expected, consolidated.toTranscribeNodeForComparison())
    }

    @Test
    fun consolidate_completeHtmlBlock_noChange() {
        val markdown = "<details><summary>Summary</summary>Content</details>"

        val parser = defaultMarkdownParser()
        val rootNode = parser.buildMarkdownTreeFromString(markdown)
        val consolidated = HtmlBlockConsolidator.consolidate(rootNode, markdown)

        val expected = TranscribeASTNode(
            type = MarkdownElementTypes.MARKDOWN_FILE,
            children = listOf(
                TranscribeASTNode(
                    type = MarkdownElementTypes.HTML_BLOCK,
                    children = listOf(
                        TranscribeASTNode(
                            type = MarkdownTokenTypes.HTML_BLOCK_CONTENT,
                            children = emptyList(),
                            startOffset = 0,
                            endOffset = markdown.length,
                        ),
                    ),
                    startOffset = 0,
                    endOffset = markdown.length,
                ),
            ),
            startOffset = 0,
            endOffset = markdown.length,
        )

        assertEquals(expected, consolidated.toTranscribeNodeForComparison())
    }

    @Test
    fun consolidate_multipleDetailsBlocks_consolidatesEach() {
        val markdown =
            """
            <details>
            <summary>First</summary>
            Content 1.

            More content 1.
            </details>

            <details>
            <summary>Second</summary>
            Content 2.

            More content 2.
            </details>
            """.trimIndent()

        val parser = defaultMarkdownParser()
        val rootNode = parser.buildMarkdownTreeFromString(markdown)
        val consolidated = HtmlBlockConsolidator.consolidate(rootNode, markdown)

        val firstDetailsEnd = markdown.indexOf("</details>") + "</details>".length
        val firstEolEnd = firstDetailsEnd + 1
        val secondEolEnd = firstEolEnd + 1
        val secondDetailsStart = secondEolEnd

        val expected = TranscribeASTNode(
            type = MarkdownElementTypes.MARKDOWN_FILE,
            children = listOf(
                TranscribeASTNode(
                    type = MarkdownElementTypes.HTML_BLOCK,
                    children = emptyList(),
                    startOffset = 0,
                    endOffset = firstDetailsEnd,
                ),
                TranscribeASTNode(
                    type = MarkdownTokenTypes.EOL,
                    children = emptyList(),
                    startOffset = firstDetailsEnd,
                    endOffset = firstEolEnd,
                ),
                TranscribeASTNode(
                    type = MarkdownTokenTypes.EOL,
                    children = emptyList(),
                    startOffset = firstEolEnd,
                    endOffset = secondEolEnd,
                ),
                TranscribeASTNode(
                    type = MarkdownElementTypes.HTML_BLOCK,
                    children = emptyList(),
                    startOffset = secondDetailsStart,
                    endOffset = markdown.length,
                ),
            ),
            startOffset = 0,
            endOffset = markdown.length,
        )

        assertEquals(expected, consolidated.toTranscribeNodeForComparison())
    }

    @Test
    fun consolidate_nonHtmlContent_unchanged() {
        val markdown =
            """
            Regular paragraph.

            Another paragraph.
            """.trimIndent()

        val parser = defaultMarkdownParser()
        val rootNode = parser.buildMarkdownTreeFromString(markdown)
        val consolidated = HtmlBlockConsolidator.consolidate(rootNode, markdown)

        // Non-HTML content should be unchanged, so consolidated should equal original
        val expected = rootNode.toTranscribeNodeForComparison()

        assertEquals(expected, consolidated.toTranscribeNodeForComparison())
    }

    @Test
    fun consolidate_unescapedDetailsElement_unchanged() {
        val markdown = "<details><summary>Summary</summary><p>Content without blank lines</p></details>"

        val parser = defaultMarkdownParser()
        val rootNode = parser.buildMarkdownTreeFromString(markdown)
        val consolidated = HtmlBlockConsolidator.consolidate(rootNode, markdown)

        // Since the details element is completely unescaped (no unclosed tags),
        // the consolidator should bail out and return the tree unchanged
        val expected = rootNode.toTranscribeNodeForComparison()

        assertEquals(expected, consolidated.toTranscribeNodeForComparison())
    }

    /**
     * Converts an ASTNode to TranscribeASTNode, excluding the parent field to enable comparison.
     */
    private fun ASTNode.toTranscribeNodeForComparison(): TranscribeASTNode =
        TranscribeASTNode(
            type = type,
            children = children.map { it.toTranscribeNodeForComparison() },
            startOffset = startOffset,
            endOffset = endOffset,
            parent = null,
        )
}

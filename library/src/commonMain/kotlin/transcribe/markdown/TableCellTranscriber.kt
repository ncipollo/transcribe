package transcribe.markdown

import context.MarkdownContext
import data.atlassian.adf.ADFBlockNode
import data.atlassian.adf.ParagraphNode
import data.atlassian.adf.TableCellNode
import data.atlassian.adf.TableHeaderNode
import data.atlassian.adf.TextNode
import kotlinx.serialization.json.JsonNull.content
import org.intellij.markdown.ast.ASTNode
import transcribe.TranscribeResult

/**
 * Transcriber for CELL nodes (GFM) that converts markdown table cells to ADF TableCellNode or TableHeaderNode.
 */
class TableCellTranscriber(
    private val nodeMapper: MarkdownNodeMapper,
    private val isHeader: Boolean = false,
) : MarkdownTranscriber<ADFBlockNode> {
    override fun transcribe(
        input: ASTNode,
        context: MarkdownContext,
    ): TranscribeResult<ADFBlockNode> {
        // Table cells contain inline content (text, formatting, etc.)
        // Wrap in a paragraph
        val inlineResult = nodeMapper.transcribeInlineChildren(input, context)

        // Drop leading and trailing whitespace nodes
        val trimmedContent =
            inlineResult.content
                .dropWhile { it is TextNode && it.text.isBlank() }
                .dropLastWhile { it is TextNode && it.text.isBlank() }

        val resultContent = listOf(ParagraphNode(content = trimmedContent))

        return if (isHeader) {
            TranscribeResult(
                TableHeaderNode(
                    content = resultContent,
                ),
                inlineResult.actions,
            )
        } else {
            TranscribeResult(
                TableCellNode(
                    content = resultContent,
                ),
                inlineResult.actions,
            )
        }
    }
}

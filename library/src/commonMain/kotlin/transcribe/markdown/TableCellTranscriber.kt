package transcribe.markdown

import data.atlassian.adf.ADFBlockNode
import data.atlassian.adf.ParagraphNode
import data.atlassian.adf.TableCellNode
import data.atlassian.adf.TableHeaderNode
import org.intellij.markdown.ast.ASTNode
import org.intellij.markdown.flavours.gfm.GFMElementTypes
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
        // Table cells typically contain inline content (text, formatting, etc.)
        // Wrap in a paragraph if needed
        val blockContent = nodeMapper.transcribeBlockChildren(input, context)

        // If no block content, create an empty paragraph
        val content = if (blockContent.isEmpty()) {
            listOf(ParagraphNode(content = emptyList()))
        } else {
            blockContent
        }

        return if (isHeader) {
            TranscribeResult(
                TableHeaderNode(
                    content = content,
                ),
            )
        } else {
            TranscribeResult(
                TableCellNode(
                    content = content,
                ),
            )
        }
    }
}


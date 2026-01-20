package transcribe.markdown

import context.MarkdownContext
import data.atlassian.adf.ADFBlockNode
import data.atlassian.adf.ADFNode
import data.atlassian.adf.BlockTaskItemNode
import data.atlassian.adf.ParagraphNode
import data.atlassian.adf.TaskItemAttrs
import data.atlassian.adf.TaskItemNode
import data.atlassian.adf.TaskState
import data.markdown.parser.getTextContent
import org.intellij.markdown.ast.ASTNode
import org.intellij.markdown.ast.findChildOfType
import org.intellij.markdown.flavours.gfm.GFMTokenTypes
import transcribe.TranscribeResult
import transcribe.action.TranscriberAction

/**
 * Transcriber for LIST_ITEM nodes with CHECK_BOX that converts markdown checklist items to ADF TaskItemNode.
 */
class CheckListItemTranscriber(
    private val nodeMapper: MarkdownNodeMapper,
) : MarkdownTranscriber<ADFNode> {
    override fun transcribe(
        input: ASTNode,
        context: MarkdownContext,
    ): TranscribeResult<ADFNode> {
        val checkBoxNode = findCheckBoxNode(input)
        val state = if (checkBoxNode != null && isChecked(checkBoxNode, context)) {
            TaskState.DONE
        } else {
            TaskState.TODO
        }

        // Collect all content from children
        val contentNodes = mutableListOf<ADFNode>()
        val allActions = mutableListOf<TranscriberAction>()

        input.children.forEach { child ->
            nodeMapper.transcriberFor(child.type)?.let { transcriber ->
                val result = transcriber.transcribe(child, context)
                contentNodes.add(result.content)
                allActions.addAll(result.actions)
            }
        }

        // Determine if we have block content (nested lists)
        val hasBlockContent = contentNodes.any { it is ADFBlockNode && it !is ParagraphNode }

        val taskNode = if (hasBlockContent) {
            // Use BlockTaskItemNode for content with nested lists
            BlockTaskItemNode(
                attrs = TaskItemAttrs(localId = "", state = state),
                content = contentNodes.filterIsInstance<ADFBlockNode>()
            )
        } else {
            // Use TaskItemNode for inline-only content
            val inlineContent = contentNodes
                .filterIsInstance<ParagraphNode>()
                .firstOrNull()
                ?.content
            TaskItemNode(
                attrs = TaskItemAttrs(localId = "", state = state),
                content = inlineContent
            )
        }

        return TranscribeResult(taskNode, allActions)
    }

    private fun findCheckBoxNode(input: ASTNode): ASTNode? = input.findChildOfType(GFMTokenTypes.CHECK_BOX)

    private fun isChecked(
        checkBoxNode: ASTNode,
        context: MarkdownContext,
    ): Boolean =
        checkBoxNode.getTextContent(context.markdownText)
            .contains("[x]", ignoreCase = true)
}

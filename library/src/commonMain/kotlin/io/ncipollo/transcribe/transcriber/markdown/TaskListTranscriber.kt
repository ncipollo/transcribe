package io.ncipollo.transcribe.transcriber.markdown

import io.ncipollo.transcribe.context.MarkdownContext
import io.ncipollo.transcribe.data.atlassian.adf.ADFBlockNode
import io.ncipollo.transcribe.data.atlassian.adf.TaskListAttrs
import io.ncipollo.transcribe.data.atlassian.adf.TaskListNode
import org.intellij.markdown.MarkdownElementTypes
import org.intellij.markdown.ast.ASTNode
import org.intellij.markdown.ast.findChildOfType
import org.intellij.markdown.flavours.gfm.GFMTokenTypes
import io.ncipollo.transcribe.transcriber.TranscribeResult

/**
 * Utility transcriber for task lists (CHECK_BOX within LIST_ITEM) that converts markdown task lists to ADF TaskListNode.
 * This transcriber can be used to detect if a list is a task list and transcribe it accordingly.
 */
class TaskListTranscriber(
    private val nodeMapper: MarkdownNodeMapper,
) : MarkdownTranscriber<ADFBlockNode> {
    /**
     * Determines if the given AST node represents a task list by checking if all LIST_ITEM children contain CHECK_BOX nodes.
     *
     * @param input The AST node (typically UNORDERED_LIST or ORDERED_LIST)
     * @return true if all list items contain checkboxes, false otherwise
     */
    fun isTaskList(input: ASTNode): Boolean {
        val listItems = input.children.filter { it.type == MarkdownElementTypes.LIST_ITEM }

        // Empty list is not a task list
        if (listItems.isEmpty()) {
            return false
        }

        // All list items must have checkboxes for it to be a task list
        return listItems.all { itemNode ->
            itemNode.findChildOfType(GFMTokenTypes.CHECK_BOX) != null
        }
    }

    override fun transcribe(
        input: ASTNode,
        context: MarkdownContext,
    ): TranscribeResult<ADFBlockNode> {
        // Extract all LIST_ITEM children and transcribe them using CheckListItemTranscriber
        val results =
            input.children
                .filter { it.type == MarkdownElementTypes.LIST_ITEM }
                .map { itemNode ->
                    val checkListItemTranscriber = CheckListItemTranscriber(nodeMapper)
                    checkListItemTranscriber.transcribe(itemNode, context)
                }
        val taskItems = results.map { it.content }
        val actions = results.flatMap { it.actions }

        return TranscribeResult(
            TaskListNode(
                attrs = TaskListAttrs(localId = ""),
                content = taskItems,
            ),
            actions,
        )
    }
}

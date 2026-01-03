package transcribe.markdown

import context.MarkdownContext
import data.atlassian.adf.ADFBlockNode
import data.atlassian.adf.ListItemNode
import org.intellij.markdown.MarkdownElementTypes
import org.intellij.markdown.ast.ASTNode
import transcribe.TranscribeResult

/**
 * Helper object for transcribing markdown lists (both bullet and ordered).
 * Handles common logic including task list detection and list item transcription.
 */
object ListTranscriberHelper {
    /**
     * Transcribes a markdown list node, handling both task lists and regular lists.
     *
     * @param input The AST node (typically UNORDERED_LIST or ORDERED_LIST)
     * @param context The markdown context
     * @param nodeMapper The markdown node mapper
     * @param listNodeFactory Factory function to create the specific list type (BulletListNode or OrderedListNode)
     * @return TranscribeResult containing either a TaskListNode or the specific list type
     */
    fun transcribe(
        input: ASTNode,
        context: MarkdownContext,
        nodeMapper: MarkdownNodeMapper,
        listNodeFactory: (List<ListItemNode>) -> ADFBlockNode,
    ): TranscribeResult<ADFBlockNode> {
        // Check if this is a task list
        val taskListTranscriber = TaskListTranscriber(nodeMapper)
        if (taskListTranscriber.isTaskList(input)) {
            return taskListTranscriber.transcribe(input, context)
        }

        // Extract all LIST_ITEM children
        val listItems =
            input.children
                .filter { it.type == MarkdownElementTypes.LIST_ITEM }
                .map { itemNode ->
                    val listItemTranscriber = ListItemTranscriber(nodeMapper)
                    listItemTranscriber.transcribe(itemNode, context).content
                }

        return TranscribeResult(listNodeFactory(listItems))
    }
}

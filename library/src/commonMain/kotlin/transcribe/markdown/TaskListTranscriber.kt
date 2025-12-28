package transcribe.markdown

import data.atlassian.adf.ADFNode
import data.atlassian.adf.TaskItemAttrs
import data.atlassian.adf.TaskItemNode
import data.atlassian.adf.TaskListAttrs
import data.atlassian.adf.TaskListNode
import data.atlassian.adf.TaskState
import data.markdown.parser.getTextContent
import org.intellij.markdown.MarkdownElementTypes
import org.intellij.markdown.MarkdownTokenTypes
import org.intellij.markdown.ast.ASTNode
import org.intellij.markdown.flavours.gfm.GFMTokenTypes
import transcribe.TranscribeResult

/**
 * Transcriber for task lists (CHECK_BOX within LIST_ITEM) that converts markdown task lists to ADF TaskListNode.
 */
class TaskListTranscriber(
    private val nodeMapper: MarkdownNodeMapper,
) : MarkdownTranscriber<TaskListNode> {
    override fun transcribe(
        input: ASTNode,
        context: MarkdownContext,
    ): TranscribeResult<TaskListNode> {
        // Task lists are UNORDERED_LIST nodes containing LIST_ITEM nodes with CHECK_BOX children
        val taskItems =
            input.children
                .filter { it.type == MarkdownElementTypes.LIST_ITEM }
                .map { itemNode ->
                    // Find CHECK_BOX child to determine state
                    val checkBoxNode = itemNode.children.firstOrNull { it.type == GFMTokenTypes.CHECK_BOX }
                    val isChecked =
                        checkBoxNode?.getTextContent(context.markdownText)?.toString()
                            ?.contains("[x]", ignoreCase = true) == true

                    // Extract content (skip LIST_BULLET and CHECK_BOX)
                    val contentNodes =
                        itemNode.children.filter { child ->
                            child.type != MarkdownTokenTypes.LIST_BULLET &&
                                child.type != GFMTokenTypes.CHECK_BOX
                        }

                    val content = mutableListOf<ADFNode>()
                    for (child in contentNodes) {
                        when (child.type) {
                            MarkdownElementTypes.PARAGRAPH -> {
                                val paragraphTranscriber =
                                    nodeMapper.transcriberFor(child.type) as? MarkdownTranscriber<ADFNode>
                                paragraphTranscriber?.transcribe(child, context)?.content?.let { content.add(it) }
                            }

                            else -> {
                                val blockContent = nodeMapper.transcribeBlockChildren(child, context)
                                content.addAll(blockContent)
                            }
                        }
                    }

                    // Convert inline content to inline nodes
                    val inlineContent = content.filterIsInstance<data.atlassian.adf.ADFInlineNode>()

                    TaskItemNode(
                        attrs =
                            TaskItemAttrs(
                                localId = "",
                                state = if (isChecked) TaskState.DONE else TaskState.TODO,
                            ),
                        content = inlineContent,
                    )
                }

        return TranscribeResult(
            TaskListNode(
                attrs = TaskListAttrs(localId = ""),
                content = taskItems,
            ),
        )
    }
}

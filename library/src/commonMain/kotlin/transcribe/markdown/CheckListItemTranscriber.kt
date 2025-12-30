package transcribe.markdown

import data.atlassian.adf.ParagraphNode
import data.atlassian.adf.TaskItemAttrs
import data.atlassian.adf.TaskItemNode
import data.atlassian.adf.TaskState
import data.markdown.parser.getTextContent
import org.intellij.markdown.MarkdownElementTypes
import org.intellij.markdown.ast.ASTNode
import org.intellij.markdown.ast.findChildOfType
import org.intellij.markdown.flavours.gfm.GFMTokenTypes
import transcribe.TranscribeResult

/**
 * Transcriber for LIST_ITEM nodes with CHECK_BOX that converts markdown checklist items to ADF TaskItemNode.
 */
class CheckListItemTranscriber(
    private val nodeMapper: MarkdownNodeMapper,
) : MarkdownTranscriber<TaskItemNode> {
    override fun transcribe(
        input: ASTNode,
        context: MarkdownContext,
    ): TranscribeResult<TaskItemNode> {
        val checkBoxNode = findCheckBoxNode(input)
        val paragraphContent =
            findParagraphNode(input)?.let {
                val transcriber = nodeMapper.transcriberFor(it.type)
                transcriber?.transcribe(it, context)?.content
            }

        return TranscribeResult(
            TaskItemNode(
                attrs =
                    TaskItemAttrs(
                        localId = "",
                        state =
                            if (checkBoxNode != null && isChecked(checkBoxNode, context)) {
                                TaskState.DONE
                            } else {
                                TaskState.TODO
                            },
                    ),
                content = (paragraphContent as? ParagraphNode)?.content,
            ),
        )
    }

    private fun findCheckBoxNode(input: ASTNode): ASTNode? = input.findChildOfType(GFMTokenTypes.CHECK_BOX)

    private fun findParagraphNode(input: ASTNode): ASTNode? = input.findChildOfType(MarkdownElementTypes.PARAGRAPH)

    private fun isChecked(
        checkBoxNode: ASTNode,
        context: MarkdownContext,
    ): Boolean =
        checkBoxNode.getTextContent(context.markdownText)
            .contains("[x]", ignoreCase = true)
}

package transcribe.atlassian

import data.atlassian.adf.ADFBlockNode
import data.atlassian.adf.ADFInlineNode
import data.atlassian.adf.ADFNode
import data.atlassian.adf.BlockquoteNode
import data.atlassian.adf.BulletListNode
import data.atlassian.adf.CodeBlockNode
import data.atlassian.adf.EmojiNode
import data.atlassian.adf.HardBreakNode
import data.atlassian.adf.HeadingNode
import data.atlassian.adf.InlineCardNode
import data.atlassian.adf.ListItemNode
import data.atlassian.adf.MentionNode
import data.atlassian.adf.OrderedListNode
import data.atlassian.adf.ParagraphNode
import data.atlassian.adf.RuleNode
import data.atlassian.adf.TableCellNode
import data.atlassian.adf.TableHeaderNode
import data.atlassian.adf.TableNode
import data.atlassian.adf.TableRowNode
import data.atlassian.adf.TaskItemNode
import data.atlassian.adf.TaskListNode
import data.atlassian.adf.TextNode
import transcribe.TranscribeResult

/**
 * Dispatcher that routes ADF nodes to their appropriate transcribers.
 * This is used by block nodes to transcribe their child content.
 */
object ADFNodeTranscriber {
    private val textTranscriber = TextNodeTranscriber()
    private val hardBreakTranscriber = HardBreakNodeTranscriber()
    private val emojiTranscriber = EmojiNodeTranscriber()
    private val mentionTranscriber = MentionNodeTranscriber()
    private val inlineCardTranscriber = InlineCardNodeTranscriber()
    private val paragraphTranscriber = ParagraphNodeTranscriber()
    private val headingTranscriber = HeadingNodeTranscriber()
    private val bulletListTranscriber = BulletListNodeTranscriber()
    private val orderedListTranscriber = OrderedListNodeTranscriber()
    private val listItemTranscriber = ListItemNodeTranscriber()
    private val codeBlockTranscriber = CodeBlockNodeTranscriber()
    private val blockquoteTranscriber = BlockquoteNodeTranscriber()
    private val ruleTranscriber = RuleNodeTranscriber()
    private val tableTranscriber = TableNodeTranscriber()
    private val tableRowTranscriber = TableRowNodeTranscriber()
    private val tableCellTranscriber = TableCellNodeTranscriber()
    private val tableHeaderTranscriber = TableHeaderNodeTranscriber()
    private val taskListTranscriber = TaskListNodeTranscriber()
    private val taskItemTranscriber = TaskItemNodeTranscriber()

    /**
     * Transcribes an inline node to markdown string.
     */
    fun transcribeInline(node: ADFInlineNode): TranscribeResult<String> {
        return when (node) {
            is TextNode -> textTranscriber.transcribe(node)
            is HardBreakNode -> hardBreakTranscriber.transcribe(node)
            is EmojiNode -> emojiTranscriber.transcribe(node)
            is MentionNode -> mentionTranscriber.transcribe(node)
            is InlineCardNode -> inlineCardTranscriber.transcribe(node)
            is TaskItemNode -> taskItemTranscriber.transcribe(node)
            else -> TranscribeResult("")
        }
    }

    /**
     * Transcribes a block node to markdown string.
     */
    fun transcribeBlock(node: ADFBlockNode): TranscribeResult<String> {
        return when (node) {
            is ParagraphNode -> paragraphTranscriber.transcribe(node)
            is HeadingNode -> headingTranscriber.transcribe(node)
            is BulletListNode -> bulletListTranscriber.transcribe(node)
            is OrderedListNode -> orderedListTranscriber.transcribe(node)
            is ListItemNode -> listItemTranscriber.transcribe(node)
            is CodeBlockNode -> codeBlockTranscriber.transcribe(node)
            is BlockquoteNode -> blockquoteTranscriber.transcribe(node)
            is RuleNode -> ruleTranscriber.transcribe(node)
            is TableNode -> tableTranscriber.transcribe(node)
            is TableRowNode -> tableRowTranscriber.transcribe(node)
            is TableCellNode -> tableCellTranscriber.transcribe(node)
            is TableHeaderNode -> tableHeaderTranscriber.transcribe(node)
            is TaskListNode -> taskListTranscriber.transcribe(node)
            else -> TranscribeResult("")
        }
    }

    /**
     * Transcribes any ADF node to markdown string.
     */
    fun transcribe(node: ADFNode): TranscribeResult<String> {
        return when (node) {
            is ADFInlineNode -> transcribeInline(node)
            is ADFBlockNode -> transcribeBlock(node)
        }
    }
}


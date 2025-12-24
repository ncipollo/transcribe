package transcribe.atlassian

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
import kotlin.reflect.KClass
import transcribe.transcriberMap

/**
 * Creates a default map of ADF node types to their corresponding transcribers.
 * This map includes all mappings found in ADFNodeTranscriber's when statements.
 */
fun defaultADFNodeMap(): Map<KClass<out ADFNode>, ADFTranscriber<*>> {
    return transcriberMap<ADFNode, ADFTranscriber<*>> {
        // Inline nodes
        add<TextNode>(TextNodeTranscriber())
        add<HardBreakNode>(HardBreakNodeTranscriber())
        add<EmojiNode>(EmojiNodeTranscriber())
        add<MentionNode>(MentionNodeTranscriber())
        add<InlineCardNode>(InlineCardNodeTranscriber())
        add<TaskItemNode>(TaskItemNodeTranscriber())

        // Block nodes
        add<ParagraphNode>(ParagraphNodeTranscriber())
        add<HeadingNode>(HeadingNodeTranscriber())
        add<BulletListNode>(BulletListNodeTranscriber())
        add<OrderedListNode>(OrderedListNodeTranscriber())
        add<ListItemNode>(ListItemNodeTranscriber())
        add<CodeBlockNode>(CodeBlockNodeTranscriber())
        add<BlockquoteNode>(BlockquoteNodeTranscriber())
        add<RuleNode>(RuleNodeTranscriber())
        add<TableNode>(TableNodeTranscriber())
        add<TableRowNode>(TableRowNodeTranscriber())
        add<TableCellNode>(TableCellNodeTranscriber())
        add<TableHeaderNode>(TableHeaderNodeTranscriber())
        add<TaskListNode>(TaskListNodeTranscriber())
    }
}


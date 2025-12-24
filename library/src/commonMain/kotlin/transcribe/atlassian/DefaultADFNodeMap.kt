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
    // Create a mutable map first
    val map = mutableMapOf<KClass<out ADFNode>, ADFTranscriber<*>>()
    
    // Add transcribers that don't need the map (leaf nodes)
    map[TextNode::class] = TextNodeTranscriber()
    map[HardBreakNode::class] = HardBreakNodeTranscriber()
    map[EmojiNode::class] = EmojiNodeTranscriber()
    map[MentionNode::class] = MentionNodeTranscriber()
    map[InlineCardNode::class] = InlineCardNodeTranscriber()
    map[CodeBlockNode::class] = CodeBlockNodeTranscriber()
    map[RuleNode::class] = RuleNodeTranscriber()
    
    // Add transcribers that need the map (they create ADFNodeTranscriber on-demand)
    map[TaskItemNode::class] = TaskItemNodeTranscriber(map)
    map[ParagraphNode::class] = ParagraphNodeTranscriber(map)
    map[HeadingNode::class] = HeadingNodeTranscriber(map)
    map[BulletListNode::class] = BulletListNodeTranscriber(map)
    map[OrderedListNode::class] = OrderedListNodeTranscriber(map)
    map[ListItemNode::class] = ListItemNodeTranscriber(map)
    map[BlockquoteNode::class] = BlockquoteNodeTranscriber(map)
    map[TableNode::class] = TableNodeTranscriber(map)
    map[TableRowNode::class] = TableRowNodeTranscriber(map)
    map[TableCellNode::class] = TableCellNodeTranscriber(map)
    map[TableHeaderNode::class] = TableHeaderNodeTranscriber(map)
    map[TaskListNode::class] = TaskListNodeTranscriber(map)
    
    return map.toMap()
}


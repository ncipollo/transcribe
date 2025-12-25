package transcribe.atlassian

import data.atlassian.adf.BlockquoteNode
import data.atlassian.adf.BulletListNode
import data.atlassian.adf.CodeBlockNode
import data.atlassian.adf.EmojiNode
import data.atlassian.adf.ExpandNode
import data.atlassian.adf.HardBreakNode
import data.atlassian.adf.HeadingNode
import data.atlassian.adf.InlineCardNode
import data.atlassian.adf.ListItemNode
import data.atlassian.adf.MediaSingleNode
import data.atlassian.adf.MentionNode
import data.atlassian.adf.NestedExpandNode
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

/**
 * Creates a default mapper of ADF node types to their corresponding transcriber factories.
 * This mapper includes all mappings found in ADFNodeTranscriber's when statements.
 */
fun defaultADFNodeMapper(): ADFNodeMapper {
    return adfNodeMapper {
        // Leaf node transcribers (don't need mapper parameter)
        add<TextNode> { TextNodeTranscriber() }
        add<HardBreakNode> { HardBreakNodeTranscriber() }
        add<EmojiNode> { EmojiNodeTranscriber() }
        add<MentionNode> { MentionNodeTranscriber() }
        add<InlineCardNode> { InlineCardNodeTranscriber() }
        add<CodeBlockNode> { CodeBlockNodeTranscriber() }
        add<RuleNode> { RuleNodeTranscriber() }
        add<MediaSingleNode> { MediaSingleNodeTranscriber() }

        // Container node transcribers (need mapper parameter)
        add<TaskItemNode> { mapper -> TaskItemNodeTranscriber(mapper) }
        add<ParagraphNode> { mapper -> ParagraphNodeTranscriber(mapper) }
        add<HeadingNode> { mapper -> HeadingNodeTranscriber(mapper) }
        add<BulletListNode> { mapper -> BulletListNodeTranscriber(mapper) }
        add<OrderedListNode> { mapper -> OrderedListNodeTranscriber(mapper) }
        add<ListItemNode> { mapper -> ListItemNodeTranscriber(mapper) }
        add<BlockquoteNode> { mapper -> BlockquoteNodeTranscriber(mapper) }
        add<ExpandNode> { mapper -> ExpandNodeTranscriber(mapper) }
        add<NestedExpandNode> { mapper -> NestedExpandNodeTranscriber(mapper) }
        add<TableNode> { mapper -> TableNodeTranscriber(mapper) }
        add<TableRowNode> { mapper -> TableRowNodeTranscriber(mapper) }
        add<TableCellNode> { mapper -> TableCellNodeTranscriber(mapper) }
        add<TableHeaderNode> { mapper -> TableHeaderNodeTranscriber(mapper) }
        add<TaskListNode> { mapper -> TaskListNodeTranscriber(mapper) }
    }
}

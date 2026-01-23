package io.ncipollo.transcribe.transcriber.atlassian

import io.ncipollo.transcribe.data.atlassian.adf.BlockTaskItemNode
import io.ncipollo.transcribe.data.atlassian.adf.BlockquoteNode
import io.ncipollo.transcribe.data.atlassian.adf.BodiedExtensionNode
import io.ncipollo.transcribe.data.atlassian.adf.BulletListNode
import io.ncipollo.transcribe.data.atlassian.adf.CodeBlockNode
import io.ncipollo.transcribe.data.atlassian.adf.EmojiNode
import io.ncipollo.transcribe.data.atlassian.adf.ExpandNode
import io.ncipollo.transcribe.data.atlassian.adf.ExtensionNode
import io.ncipollo.transcribe.data.atlassian.adf.HardBreakNode
import io.ncipollo.transcribe.data.atlassian.adf.HeadingNode
import io.ncipollo.transcribe.data.atlassian.adf.InlineCardNode
import io.ncipollo.transcribe.data.atlassian.adf.InlineExtensionNode
import io.ncipollo.transcribe.data.atlassian.adf.ListItemNode
import io.ncipollo.transcribe.data.atlassian.adf.MediaSingleNode
import io.ncipollo.transcribe.data.atlassian.adf.MentionNode
import io.ncipollo.transcribe.data.atlassian.adf.NestedExpandNode
import io.ncipollo.transcribe.data.atlassian.adf.OrderedListNode
import io.ncipollo.transcribe.data.atlassian.adf.ParagraphNode
import io.ncipollo.transcribe.data.atlassian.adf.RuleNode
import io.ncipollo.transcribe.data.atlassian.adf.StatusNode
import io.ncipollo.transcribe.data.atlassian.adf.TableCellNode
import io.ncipollo.transcribe.data.atlassian.adf.TableHeaderNode
import io.ncipollo.transcribe.data.atlassian.adf.TableNode
import io.ncipollo.transcribe.data.atlassian.adf.TableRowNode
import io.ncipollo.transcribe.data.atlassian.adf.TaskItemNode
import io.ncipollo.transcribe.data.atlassian.adf.TaskListNode
import io.ncipollo.transcribe.data.atlassian.adf.TextNode

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
        add<StatusNode> { StatusNodeTranscriber() }
        add<CodeBlockNode> { CodeBlockNodeTranscriber() }
        add<RuleNode> { RuleNodeTranscriber() }
        add<MediaSingleNode> { MediaSingleNodeTranscriber() }
        add<ExtensionNode> { ExtensionNodeTranscriber() }
        add<BodiedExtensionNode> { BodiedExtensionNodeTranscriber() }
        add<InlineExtensionNode> { InlineExtensionNodeTranscriber() }

        // Container node transcribers (need mapper parameter)
        add<TaskItemNode> { mapper -> TaskItemNodeTranscriber(mapper) }
        add<BlockTaskItemNode> { mapper -> BlockTaskItemNodeTranscriber(mapper) }
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

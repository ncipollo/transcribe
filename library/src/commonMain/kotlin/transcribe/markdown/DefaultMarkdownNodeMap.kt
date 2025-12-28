package transcribe.markdown

import org.intellij.markdown.MarkdownElementTypes
import org.intellij.markdown.MarkdownTokenTypes
import org.intellij.markdown.flavours.gfm.GFMElementTypes

/**
 * Creates a default mapper of markdown element types to their corresponding transcriber factories.
 * This mapper includes all mappings for standard markdown and GFM element types.
 */
fun defaultMarkdownNodeMapper(): MarkdownNodeMapper {
    return markdownNodeMapper {
        // Leaf node transcribers (don't need dependencies)
        add(MarkdownTokenTypes.TEXT) { TextTranscriber() }
        add(MarkdownElementTypes.CODE_SPAN) { CodeSpanTranscriber() }
        add(MarkdownElementTypes.CODE_FENCE) { CodeBlockTranscriber() }
        add(MarkdownElementTypes.CODE_BLOCK) { CodeBlockTranscriber() }

        // Inline content transcribers (need InlineContentTranscriber)
        add(MarkdownElementTypes.EMPH) {
            val inlineTranscriber = InlineContentTranscriber()
            EmphasisTranscriber(inlineTranscriber)
        }
        add(MarkdownElementTypes.STRONG) {
            val inlineTranscriber = InlineContentTranscriber()
            StrongTranscriber(inlineTranscriber)
        }
        add(MarkdownElementTypes.INLINE_LINK) {
            val inlineTranscriber = InlineContentTranscriber()
            LinkTranscriber(inlineTranscriber)
        }
        add(MarkdownElementTypes.FULL_REFERENCE_LINK) {
            val inlineTranscriber = InlineContentTranscriber()
            LinkTranscriber(inlineTranscriber)
        }
        add(MarkdownElementTypes.SHORT_REFERENCE_LINK) {
            val inlineTranscriber = InlineContentTranscriber()
            LinkTranscriber(inlineTranscriber)
        }
        add(MarkdownElementTypes.AUTOLINK) {
            val inlineTranscriber = InlineContentTranscriber()
            LinkTranscriber(inlineTranscriber)
        }
        add(GFMElementTypes.STRIKETHROUGH) {
            val inlineTranscriber = InlineContentTranscriber()
            StrikethroughTranscriber(inlineTranscriber)
        }

        // Block content transcribers (need MarkdownNodeMapper)
        add(MarkdownElementTypes.PARAGRAPH) {
            val inlineTranscriber = InlineContentTranscriber()
            ParagraphTranscriber(inlineTranscriber)
        }
        add(MarkdownElementTypes.ATX_1) {
            val inlineTranscriber = InlineContentTranscriber()
            HeadingTranscriber(inlineTranscriber)
        }
        add(MarkdownElementTypes.ATX_2) {
            val inlineTranscriber = InlineContentTranscriber()
            HeadingTranscriber(inlineTranscriber)
        }
        add(MarkdownElementTypes.ATX_3) {
            val inlineTranscriber = InlineContentTranscriber()
            HeadingTranscriber(inlineTranscriber)
        }
        add(MarkdownElementTypes.ATX_4) {
            val inlineTranscriber = InlineContentTranscriber()
            HeadingTranscriber(inlineTranscriber)
        }
        add(MarkdownElementTypes.ATX_5) {
            val inlineTranscriber = InlineContentTranscriber()
            HeadingTranscriber(inlineTranscriber)
        }
        add(MarkdownElementTypes.ATX_6) {
            val inlineTranscriber = InlineContentTranscriber()
            HeadingTranscriber(inlineTranscriber)
        }
        add(MarkdownElementTypes.SETEXT_1) {
            val inlineTranscriber = InlineContentTranscriber()
            HeadingTranscriber(inlineTranscriber)
        }
        add(MarkdownElementTypes.SETEXT_2) {
            val inlineTranscriber = InlineContentTranscriber()
            HeadingTranscriber(inlineTranscriber)
        }
        add(MarkdownElementTypes.UNORDERED_LIST) {
            BulletListTranscriber(it)
        }
        add(MarkdownElementTypes.ORDERED_LIST) {
            OrderedListTranscriber(it)
        }
        add(MarkdownElementTypes.LIST_ITEM) {
            ListItemTranscriber(it)
        }
        add(MarkdownElementTypes.BLOCK_QUOTE) {
            BlockquoteTranscriber(it)
        }
        add(MarkdownElementTypes.IMAGE) {
            val inlineTranscriber = InlineContentTranscriber()
            ImageTranscriber(inlineTranscriber)
        }
        add(GFMElementTypes.TABLE) {
            TableTranscriber(it)
        }
    }
}

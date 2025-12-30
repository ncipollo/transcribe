package transcribe.markdown

import org.intellij.markdown.MarkdownElementTypes
import org.intellij.markdown.MarkdownTokenTypes
import org.intellij.markdown.flavours.gfm.GFMElementTypes
import org.intellij.markdown.flavours.gfm.GFMTokenTypes

/**
 * Creates a default mapper of markdown element types to their corresponding transcriber factories.
 * This mapper includes all mappings for standard markdown and GFM element types.
 */
fun defaultMarkdownNodeMapper(): MarkdownNodeMapper {
    return markdownNodeMapper {
        // Leaf node transcribers (don't need dependencies)
        add(MarkdownTokenTypes.TEXT) { TextTranscriber() }
        add(MarkdownTokenTypes.EOL) { EolTranscriber() }
        add(MarkdownTokenTypes.WHITE_SPACE) { WhitespaceTranscriber() }
        add(MarkdownElementTypes.CODE_SPAN) { CodeSpanTranscriber() }
        add(MarkdownElementTypes.CODE_FENCE) { CodeFenceTranscriber() }
        add(MarkdownElementTypes.CODE_BLOCK) { CodeBlockTranscriber() }

        // Inline content transcribers (need MarkdownNodeMapper)
        add(MarkdownElementTypes.EMPH) {
            EmphasisTranscriber()
        }
        add(MarkdownElementTypes.STRONG) {
            StrongTranscriber()
        }
        add(MarkdownElementTypes.INLINE_LINK) {
            LinkTranscriber()
        }
        add(MarkdownElementTypes.AUTOLINK) {
            LinkTranscriber()
        }
        add(GFMTokenTypes.GFM_AUTOLINK) {
            LinkTranscriber()
        }
        add(GFMElementTypes.STRIKETHROUGH) {
            StrikethroughTranscriber()
        }

        // Block content transcribers (need MarkdownNodeMapper)
        add(MarkdownElementTypes.PARAGRAPH) {
            ParagraphTranscriber(it)
        }
        add(MarkdownElementTypes.ATX_1) {
            HeadingTranscriber(it)
        }
        add(MarkdownElementTypes.ATX_2) {
            HeadingTranscriber(it)
        }
        add(MarkdownElementTypes.ATX_3) {
            HeadingTranscriber(it)
        }
        add(MarkdownElementTypes.ATX_4) {
            HeadingTranscriber(it)
        }
        add(MarkdownElementTypes.ATX_5) {
            HeadingTranscriber(it)
        }
        add(MarkdownElementTypes.ATX_6) {
            HeadingTranscriber(it)
        }
        add(MarkdownElementTypes.SETEXT_1) {
            HeadingTranscriber(it)
        }
        add(MarkdownElementTypes.SETEXT_2) {
            HeadingTranscriber(it)
        }
        add(MarkdownElementTypes.UNORDERED_LIST) {
            BulletListTranscriber(it)
        }
        add(MarkdownElementTypes.ORDERED_LIST) {
            OrderedListTranscriber(it)
        }
        add(MarkdownElementTypes.BLOCK_QUOTE) {
            BlockquoteTranscriber(it)
        }
        add(MarkdownElementTypes.IMAGE) {
            ImageTranscriber()
        }
        add(GFMElementTypes.TABLE) {
            TableTranscriber(it)
        }
    }
}

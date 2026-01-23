package io.ncipollo.transcribe.data.markdown.parser

import org.intellij.markdown.MarkdownElementTypes
import kotlin.test.Test
import kotlin.test.assertEquals

class TranscribeASTNodeTest {
    @Test
    fun toTranscribeNode_convertsSimpleNode() {
        val markdown = "# Heading"
        val document = MarkdownDocument.create(markdown)
        val heading = document.rootNode.findChildOfTypeInSubtree(MarkdownElementTypes.ATX_1)
            ?: error("Should find heading")

        val transcribed = heading.toTranscribeNode()

        assertEquals(heading.type, transcribed.type)
        assertEquals(heading.startOffset, transcribed.startOffset)
        assertEquals(heading.endOffset, transcribed.endOffset)
        assertEquals(heading.children.size, transcribed.children.size)
    }

    @Test
    fun copyWithChildren_createsNewNodeWithChildren() {
        val markdown = "# Heading"
        val document = MarkdownDocument.create(markdown)
        val heading = document.rootNode.findChildOfTypeInSubtree(MarkdownElementTypes.ATX_1)
            ?: error("Should find heading")

        val newChildren = emptyList<TranscribeASTNode>()
        val copied = heading.copyWithChildren(newChildren)

        assertEquals(heading.type, copied.type)
        assertEquals(heading.startOffset, copied.startOffset)
        assertEquals(heading.endOffset, copied.endOffset)
        assertEquals(0, copied.children.size)
    }

    @Test
    fun copyWithOffsets_createsNewNodeWithOffsets() {
        val markdown = "# Heading"
        val document = MarkdownDocument.create(markdown)
        val heading = document.rootNode.findChildOfTypeInSubtree(MarkdownElementTypes.ATX_1)
            ?: error("Should find heading")

        val copied = heading.copyWithOffsets(0, 100)

        assertEquals(heading.type, copied.type)
        assertEquals(0, copied.startOffset)
        assertEquals(100, copied.endOffset)
        assertEquals(heading.children.size, copied.children.size)
    }
}

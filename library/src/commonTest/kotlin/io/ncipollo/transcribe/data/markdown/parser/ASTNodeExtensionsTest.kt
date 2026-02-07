package io.ncipollo.transcribe.data.markdown.parser

import org.intellij.markdown.MarkdownElementTypes
import org.intellij.markdown.ast.ASTNode
import org.intellij.markdown.ast.findChildOfType
import org.intellij.markdown.flavours.gfm.GFMFlavourDescriptor
import org.intellij.markdown.parser.MarkdownParser
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class ASTNodeExtensionsTest {
    private fun parseMarkdown(text: String): ASTNode {
        val parser = MarkdownParser(GFMFlavourDescriptor())
        return parser.buildMarkdownTreeFromString(text)
    }

    @Test
    fun getTextContent_extractsTextFromSimpleNode() {
        val text = "Hello world"
        val root = parseMarkdown(text)

        // Find a text node or paragraph
        val paragraph = root.findChildOfType(MarkdownElementTypes.PARAGRAPH)
        assertNotNull(paragraph)

        val extractedText = paragraph.getTextContent(text)

        assertNotNull(extractedText, "Should extract text")
        assertEquals("Hello world", extractedText.toString().trim(), "Should extract correct text")
    }

    @Test
    fun getTextContent_extractsTextFromNodeAtDifferentOffsets() {
        val text = "First paragraph\n\nSecond paragraph"
        val root = parseMarkdown(text)

        // Find paragraphs
        val paragraphs = root.children.filter { it.type == MarkdownElementTypes.PARAGRAPH }

        if (paragraphs.isNotEmpty()) {
            val firstParagraph = paragraphs[0]
            val extractedText = firstParagraph.getTextContent(text)

            assertNotNull(extractedText, "Should extract text from first paragraph")
            assertEquals("First paragraph", extractedText.toString().trim(), "Should extract correct text")
        }
    }

    @Test
    fun getTextContent_handlesEmptyNode() {
        val text = ""
        val root = parseMarkdown(text)

        val extractedText = root.getTextContent(text)

        assertNotNull(extractedText, "Should handle empty node")
        assertEquals("", extractedText.toString(), "Should return empty string for empty node")
    }

    @Test
    fun findImageNodes_emptyNodeList() {
        val text = ""
        val root = parseMarkdown(text)

        val imageNodes = root.findImageNodes()

        assertEquals(0, imageNodes.size)
    }

    @Test
    fun findImageNodes_listContainsNodesButNoImageNodes() {
        val text = "Hello world\n\nThis is a paragraph with no images."
        val root = parseMarkdown(text)

        val imageNodes = root.findImageNodes()

        assertEquals(0, imageNodes.size)
    }

    @Test
    fun findImageNodes_listContains2ImageNodes() {
        val text = "![Alt text 1](image1.png)\n\n![Alt text 2](image2.png)"
        val root = parseMarkdown(text)

        val imageNodes = root.findImageNodes()

        assertEquals(2, imageNodes.size)
    }

    @Test
    fun getTextContentWithoutDelimiters_symmetricDelimiters() {
        val text = "**bold text**"
        val root = parseMarkdown(text)
        val strongNode = root.findChildOfTypeInSubtree(MarkdownElementTypes.STRONG)

        assertNotNull(strongNode)
        val result = strongNode.getTextContentWithoutDelimiters(text, "**")

        assertEquals("bold text", result)
    }

    @Test
    fun getTextContentWithoutDelimiters_asymmetricDelimiters() {
        val text = "[link text](url)"
        val root = parseMarkdown(text)
        val linkNode = root.findChildOfTypeInSubtree(MarkdownElementTypes.INLINE_LINK)
        val linkTextNode = linkNode?.findChildOfTypeInSubtree(MarkdownElementTypes.LINK_TEXT)

        assertNotNull(linkTextNode)
        val result = linkTextNode.getTextContentWithoutDelimiters(text, "[", "]")

        assertEquals("link text", result)
    }

    @Test
    fun getTextContentWithoutDelimiters_withCommaAndSpaces() {
        val text = "**bold, text with spaces**"
        val root = parseMarkdown(text)
        val strongNode = root.findChildOfTypeInSubtree(MarkdownElementTypes.STRONG)

        assertNotNull(strongNode)
        val result = strongNode.getTextContentWithoutDelimiters(text, "**")

        assertEquals("bold, text with spaces", result)
    }

    @Test
    fun getTextContentWithoutDelimiters_strikethrough() {
        val text = "~~strikethrough~~"
        val root = parseMarkdown(text)
        val strikeNode = root.findChildOfTypeInSubtree(org.intellij.markdown.flavours.gfm.GFMElementTypes.STRIKETHROUGH)

        assertNotNull(strikeNode)
        val result = strikeNode.getTextContentWithoutDelimiters(text, "~~")

        assertEquals("strikethrough", result)
    }

    @Test
    fun getTextContentWithoutDelimiters_emphasis() {
        val text = "*italic*"
        val root = parseMarkdown(text)
        val emphNode = root.findChildOfTypeInSubtree(MarkdownElementTypes.EMPH)

        assertNotNull(emphNode)
        val result = emphNode.getTextContentWithoutDelimiters(text, "*")

        assertEquals("italic", result)
    }
}

package data.markdown.parser

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import org.intellij.markdown.ast.ASTNode
import org.intellij.markdown.MarkdownElementTypes
import org.intellij.markdown.MarkdownTokenTypes
import org.intellij.markdown.flavours.gfm.GFMFlavourDescriptor
import org.intellij.markdown.parser.MarkdownParser

class ASTNodeExtensionsTest {

    private fun parseMarkdown(text: String): ASTNode {
        val parser = MarkdownParser(GFMFlavourDescriptor())
        return parser.buildMarkdownTreeFromString(text)
    }

    @Test
    fun findChildOfType_findsExistingChild() {
        val text = "# Heading\n\nParagraph text"
        val root = parseMarkdown(text)
        
        // Find a heading child in the document
        val heading = root.findChildOfType(MarkdownElementTypes.ATX_1)
        
        assertNotNull(heading, "Should find heading child")
    }

    @Test
    fun findChildOfType_returnsNullWhenChildTypeNotFound() {
        val text = "Simple paragraph text"
        val root = parseMarkdown(text)
        
        // Try to find a heading when there isn't one
        val heading = root.findChildOfType(MarkdownElementTypes.ATX_1)
        
        assertNull(heading, "Should return null when child type not found")
    }

    @Test
    fun getParentOfType_findsImmediateParent() {
        val text = "# Heading\n\nParagraph text"
        val root = parseMarkdown(text)
        
        // Find a heading node
        val heading = root.findChildOfType(MarkdownElementTypes.ATX_1)
        assertNotNull(heading)
        
        // Get its parent (should be the root or a containing block)
        val parent = heading.getParentOfType(MarkdownElementTypes.MARKDOWN_FILE)
        
        assertNotNull(parent, "Should find parent")
    }

    @Test
    fun getParentOfType_findsAncestorMultipleLevelsUp() {
        val text = "> Blockquote\n> > Nested blockquote"
        val root = parseMarkdown(text)
        
        // Navigate to find nested structure
        val firstBlockquote = root.findChildOfType(MarkdownElementTypes.BLOCK_QUOTE)
        assertNotNull(firstBlockquote)
        
        // Find nested blockquote within
        val nestedBlockquote = firstBlockquote.children.firstOrNull {
            it.type == MarkdownTokenTypes.BLOCK_QUOTE
        }

        val parentBlockquote = nestedBlockquote?.getParentOfType(MarkdownElementTypes.BLOCK_QUOTE)
        assertNotNull(parentBlockquote, "Should find ancestor blockquote")
    }

    @Test
    fun getParentOfType_withMultipleTypeParameters() {
        val text = "# Heading\n\nParagraph text"
        val root = parseMarkdown(text)
        
        val heading = root.findChildOfType(MarkdownElementTypes.ATX_1)
        assertNotNull(heading)
        
        // Search for multiple possible parent types
        val parent = heading.getParentOfType(
            MarkdownElementTypes.MARKDOWN_FILE,
            MarkdownElementTypes.PARAGRAPH
        )
        
        assertNotNull(parent, "Should find parent matching one of the types")
    }

    @Test
    fun getParentOfType_returnsNullWhenNoMatchingParent() {
        val text = "Simple text"
        val root = parseMarkdown(text)
        
        // Try to find a parent that doesn't exist in the tree
        val parent = root.getParentOfType(MarkdownElementTypes.ATX_1)
        
        assertNull(parent, "Should return null when no matching parent exists")
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
}


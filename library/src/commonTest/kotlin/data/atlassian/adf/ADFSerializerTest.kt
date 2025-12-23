package data.atlassian.adf

import fixtures.ADFFixture
import org.intellij.markdown.lexer.Compat.assert
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNotNull

class ADFSerializerTest {
    @Test
    fun testRoundTripSerialization() {
        // Load fixture
        val fixtureJson = ADFFixture.sampleDocument

        // Parse JSON to DocNode
        val docNode = ADFSerializer.fromJson(fixtureJson)

        // Verify it's a DocNode
        assertIs<DocNode>(docNode)
        assertEquals(1, docNode.version)
        assertNotNull(docNode.content)
        assert(docNode.content.isNotEmpty())

        // Serialize back to JSON
        val serializedJson = ADFSerializer.toJson(docNode)

        // Parse again
        val docNode2 = ADFSerializer.fromJson(serializedJson)

        // Verify round-trip equality
        assertEquals(docNode.version, docNode2.version)
        assertEquals(docNode.content.size, docNode2.content.size)

        // Verify known node types are correctly typed
        val firstParagraph = docNode.content.firstOrNull { it is ParagraphNode } as? ParagraphNode
        assertNotNull(firstParagraph)
        assertEquals("This content exists outside of a section.", (firstParagraph.content?.first() as? TextNode)?.text)

        val firstHeading = docNode.content.firstOrNull { it is HeadingNode } as? HeadingNode
        assertNotNull(firstHeading)
        assertEquals(1, firstHeading.attrs.level)
        assertEquals("Section 1", (firstHeading.content?.first() as? TextNode)?.text)

        // Verify unknown node types are captured as UnknownBlockNode with attrs and content
        val taskList = docNode.content.firstOrNull { it is UnknownBlockNode && it.type == "taskList" } as? UnknownBlockNode
        assertNotNull(taskList, "taskList should be captured as UnknownBlockNode")
        assertEquals("taskList", taskList.type)
        assertNotNull(taskList.attrs, "taskList should have attrs")
        assertNotNull(taskList.content, "taskList should have content")

        val mediaSingle = docNode.content.firstOrNull { it is UnknownBlockNode && it.type == "mediaSingle" } as? UnknownBlockNode
        assertNotNull(mediaSingle, "mediaSingle should be captured as UnknownBlockNode")
        assertEquals("mediaSingle", mediaSingle.type)
        assertNotNull(mediaSingle.attrs, "mediaSingle should have attrs")
        assertNotNull(mediaSingle.content, "mediaSingle should have content")

        val expand = docNode.content.firstOrNull { it is UnknownBlockNode && it.type == "expand" } as? UnknownBlockNode
        assertNotNull(expand, "expand should be captured as UnknownBlockNode")
        assertEquals("expand", expand.type)
        assertNotNull(expand.attrs, "expand should have attrs")
        assertNotNull(expand.content, "expand should have content")

        // Verify unknown inline nodes are captured as UnknownInlineNode with attrs
        // Status nodes appear inside paragraphs, so we need to find them
        val statusNode = docNode.content
            .flatMap { block ->
                when (block) {
                    is BulletListNode -> block.content.flatMap { item ->
                        item.content.filterIsInstance<ParagraphNode>()
                            .flatMap { para -> para.content ?: emptyList() }
                    }
                    else -> emptyList()
                }
            }
            .firstOrNull { it is UnknownInlineNode && it.type == "status" } as? UnknownInlineNode

        assertNotNull(statusNode, "status should be captured as UnknownInlineNode")
        assertEquals("status", statusNode.type)
        assertNotNull(statusNode.attrs, "status should have attrs")
    }
}


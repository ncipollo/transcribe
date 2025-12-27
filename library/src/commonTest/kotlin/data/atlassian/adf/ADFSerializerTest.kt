package data.atlassian.adf

import fixtures.adf.ComplexADFDocumentFixture
import fixtures.adf.SimpleADFDocumentFixture
import kotlin.test.Test
import kotlin.test.assertEquals

class ADFSerializerTest {
    @Test
    fun testRoundTripPreservesElementCount() {
        // Parse JSON to DocNode
        val docNode = ADFSerializer.fromJson(ComplexADFDocumentFixture.COMPLEX_DOCUMENT)
        val initialCount = docNode.content.size

        // Serialize back to JSON and parse again
        val serializedJson = ADFSerializer.toJson(docNode)
        val docNode2 = ADFSerializer.fromJson(serializedJson)

        // Verify round-trip preserves element count
        assertEquals(initialCount, docNode2.content.size)
    }

    @Test
    fun testParsingMatchesExpectedNodes() {
        val parsedDoc = ADFSerializer.fromJson(SimpleADFDocumentFixture.SIMPLE_DOCUMENT)
        val expected = SimpleADFDocumentFixture.expectedSimpleDocNode
        assertEquals(expected, parsedDoc)
    }
}

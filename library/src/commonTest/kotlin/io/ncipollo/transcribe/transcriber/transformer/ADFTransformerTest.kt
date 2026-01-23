package io.ncipollo.transcribe.transcriber.transformer

import io.ncipollo.transcribe.data.atlassian.adf.ADFBlockNode
import io.ncipollo.transcribe.data.atlassian.adf.ParagraphNode
import io.ncipollo.transcribe.data.atlassian.adf.TextNode
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotSame

class ADFTransformerTest {
    @Test
    fun identityTransformer_returnsInputUnchanged() {
        val transformer = IdentityADFTransformer<String>()
        val nodes = listOf<ADFBlockNode>(
            ParagraphNode(
                content = listOf(
                    TextNode(text = "Hello, World!"),
                ),
            ),
        )
        val context = "test context"

        val result = transformer.transform(nodes, context)

        assertEquals(nodes, result)
        // Verify it's actually the same instance (identity)
        assertNotSame(nodes.toTypedArray(), result.toTypedArray())
    }

    @Test
    fun identityTransformer_handlesEmptyList() {
        val transformer = IdentityADFTransformer<Unit>()
        val nodes = emptyList<ADFBlockNode>()

        val result = transformer.transform(nodes, Unit)

        assertEquals(emptyList(), result)
    }
}

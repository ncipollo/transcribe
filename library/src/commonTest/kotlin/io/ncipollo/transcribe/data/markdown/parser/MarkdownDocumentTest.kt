package io.ncipollo.transcribe.data.markdown.parser

import io.ncipollo.transcribe.fixtures.markdown.ComplexMarkdownFixture
import kotlin.test.Test

class MarkdownDocumentTest {
    @Test
    fun create_complexMarkdown() {
        val document = MarkdownDocument.create(ComplexMarkdownFixture.COMPLEX_MARKDOWN)
        document.rootNode.printTree()
    }
}

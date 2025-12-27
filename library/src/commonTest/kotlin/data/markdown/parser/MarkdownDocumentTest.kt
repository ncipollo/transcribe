package data.markdown.parser

import fixtures.markdown.ComplexMarkdownFixture
import kotlin.test.Test

class MarkdownDocumentTest {
    @Test
    fun create_complexMarkdown() {
        val document = MarkdownDocument.create(ComplexMarkdownFixture.COMPLEX_MARKDOWN)
        println(document.rootNode)
    }
}


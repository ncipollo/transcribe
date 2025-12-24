package data.markdown.parser

import org.intellij.markdown.parser.MarkdownParser

data class MarkdownDocument(
    val parser: MarkdownParser,
    val text: String,
)


package io.ncipollo.transcribe.data.markdown.parser

import org.intellij.markdown.ast.ASTNode
import org.intellij.markdown.parser.MarkdownParser
import kotlin.jvm.JvmStatic

data class MarkdownDocument(
    val rootNode: ASTNode,
    val text: String,
) {
    companion object {
        @JvmStatic
        fun create(
            markdownText: String,
            markdownParser: MarkdownParser = defaultMarkdownParser(),
        ): MarkdownDocument {
            val rootNode = markdownParser.buildMarkdownTreeFromString(markdownText)
            val consolidatedRoot = HtmlBlockConsolidator.consolidate(rootNode, markdownText)
            return MarkdownDocument(rootNode = consolidatedRoot, text = markdownText)
        }
    }
}

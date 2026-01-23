package io.ncipollo.transcribe.transcriber.markdown

import io.ncipollo.transcribe.context.MarkdownContext
import io.ncipollo.transcribe.data.atlassian.adf.CodeBlockAttrs
import io.ncipollo.transcribe.data.atlassian.adf.CodeBlockNode
import io.ncipollo.transcribe.data.atlassian.adf.TextNode
import org.intellij.markdown.MarkdownElementTypes
import kotlin.test.Test
import kotlin.test.assertEquals

class CodeBlockTranscriberTest {
    private val transcriber = CodeBlockTranscriber()

    @Test
    fun transcribe_indentedCodeBlock() {
        val markdown =
            """
            |    val x = 1
            |    val y = 2
            |    val z = x + y
            """.trimMargin()
        val codeBlockNode = MarkdownTestHelper.findNode(markdown, MarkdownElementTypes.CODE_BLOCK)
        val context = MarkdownContext(markdownText = markdown)
        val result = transcriber.transcribe(codeBlockNode, context)

        val expected =
            CodeBlockNode(
                attrs = CodeBlockAttrs(language = null),
                content =
                listOf(
                    TextNode(text = "val x = 1\nval y = 2\nval z = x + y"),
                ),
            )
        assertEquals(expected, result.content)
    }
}

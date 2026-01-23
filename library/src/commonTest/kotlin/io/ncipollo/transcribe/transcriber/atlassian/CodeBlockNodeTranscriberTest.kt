package io.ncipollo.transcribe.transcriber.atlassian

import io.ncipollo.transcribe.context.ADFTranscriberContext
import io.ncipollo.transcribe.data.atlassian.adf.CodeBlockAttrs
import io.ncipollo.transcribe.data.atlassian.adf.CodeBlockNode
import io.ncipollo.transcribe.data.atlassian.adf.TextNode
import kotlin.test.Test
import kotlin.test.assertEquals

class CodeBlockNodeTranscriberTest {
    private val transcriber = CodeBlockNodeTranscriber()
    private val context = ADFTranscriberContext()

    @Test
    fun transcribe_withLanguage() {
        val node =
            CodeBlockNode(
                attrs = CodeBlockAttrs(language = "kotlin"),
                content =
                listOf(
                    TextNode(text = "fun main() {"),
                    TextNode(text = "    println(\"Hello\")"),
                    TextNode(text = "}"),
                ),
            )
        val result = transcriber.transcribe(node, context)
        assertEquals("```kotlin\nfun main() {\n    println(\"Hello\")\n}\n```\n\n", result.content)
    }

    @Test
    fun transcribe_withoutLanguage() {
        val node =
            CodeBlockNode(
                content = listOf(TextNode(text = "code here")),
            )
        val result = transcriber.transcribe(node, context)
        assertEquals("```\ncode here\n```\n\n", result.content)
    }

    @Test
    fun transcribe_emptyContent() {
        val node = CodeBlockNode(content = null)
        val result = transcriber.transcribe(node, context)
        assertEquals("```\n\n```\n\n", result.content)
    }
}

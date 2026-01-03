package transcribe.markdown

import data.atlassian.adf.CodeBlockAttrs
import data.atlassian.adf.CodeBlockNode
import data.atlassian.adf.TextNode
import org.intellij.markdown.MarkdownElementTypes
import kotlin.test.Test
import kotlin.test.assertEquals

class CodeFenceTranscriberTest {
    private val transcriber = CodeFenceTranscriber()

    @Test
    fun transcribe_codeFenceWithLanguage() {
        val markdown = "```kotlin\nval x = 1\nval y = 2\n```"
        val codeFenceNode = MarkdownTestHelper.findNode(markdown, MarkdownElementTypes.CODE_FENCE)
        val context = MarkdownContext(markdownText = markdown)
        val result = transcriber.transcribe(codeFenceNode, context)

        val expected =
            CodeBlockNode(
                attrs = CodeBlockAttrs(language = "kotlin"),
                content =
                listOf(
                    TextNode(text = "val x = 1\nval y = 2"),
                ),
            )
        assertEquals(expected, result.content)
    }

    @Test
    fun transcribe_codeFenceWithoutLanguage() {
        val markdown = "```\nsome code\n```"
        val codeFenceNode = MarkdownTestHelper.findNode(markdown, MarkdownElementTypes.CODE_FENCE)
        val context = MarkdownContext(markdownText = markdown)
        val result = transcriber.transcribe(codeFenceNode, context)

        val expected =
            CodeBlockNode(
                attrs = CodeBlockAttrs(language = null),
                content =
                listOf(
                    TextNode(text = "some code"),
                ),
            )
        assertEquals(expected, result.content)
    }
}

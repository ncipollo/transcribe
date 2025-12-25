package transcribe.atlassian

import data.atlassian.adf.BulletListNode
import data.atlassian.adf.ListItemNode
import data.atlassian.adf.ParagraphNode
import data.atlassian.adf.TextNode
import kotlin.test.Test
import kotlin.test.assertEquals

class BulletListNodeTranscriberTest {
    private val transcriber = BulletListNodeTranscriber(defaultADFNodeMapper())
    private val context = ADFTranscriberContext()

    @Test
    fun transcribe_withItems() {
        val node =
            BulletListNode(
                content =
                    listOf(
                        ListItemNode(
                            content =
                                listOf(
                                    ParagraphNode(content = listOf(TextNode(text = "First item"))),
                                ),
                        ),
                        ListItemNode(
                            content =
                                listOf(
                                    ParagraphNode(content = listOf(TextNode(text = "Second item"))),
                                ),
                        ),
                    ),
            )
        val result = transcriber.transcribe(node, context)
        assertEquals("- First item\n- Second item\n", result.content)
    }

    @Test
    fun transcribe_emptyList() {
        val node = BulletListNode(content = emptyList())
        val result = transcriber.transcribe(node, context)
        assertEquals("", result.content)
    }
}

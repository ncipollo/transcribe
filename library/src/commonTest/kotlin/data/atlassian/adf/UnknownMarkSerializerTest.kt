package data.atlassian.adf

import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import kotlin.test.Test
import kotlin.test.assertEquals

class UnknownMarkSerializerTest {
    @Test
    fun deserializeWithTypeOnly() {
        val json = """
        {
            "type": "highlight"
        }
        """

        val doc =
            ADFSerializer.fromJson(
                """
        {
            "type": "doc",
            "version": 1,
            "content": [
                {
                    "type": "paragraph",
                    "content": [
                        {
                            "type": "text",
                            "text": "Test",
                            "marks": [$json]
                        }
                    ]
                }
            ]
        }
        """,
            )

        val paragraph = doc.content.first() as ParagraphNode
        val textNode = paragraph.content?.first() as TextNode
        val actual = textNode.marks?.first()

        val expected =
            UnknownMark(
                type = "highlight",
                attrs = null,
            )

        assertEquals(expected, actual)
    }

    @Test
    fun deserializeWithAttrs() {
        val json = """
        {
            "type": "customMark",
            "attrs": {
                "color": "#FF5733",
                "weight": "bold",
                "style": "dashed"
            }
        }
        """

        val doc =
            ADFSerializer.fromJson(
                """
        {
            "type": "doc",
            "version": 1,
            "content": [
                {
                    "type": "paragraph",
                    "content": [
                        {
                            "type": "text",
                            "text": "Styled text",
                            "marks": [$json]
                        }
                    ]
                }
            ]
        }
        """,
            )

        val paragraph = doc.content.first() as ParagraphNode
        val textNode = paragraph.content?.first() as TextNode
        val actual = textNode.marks?.first()

        val expected =
            UnknownMark(
                type = "customMark",
                attrs =
                buildJsonObject {
                    put("color", "#FF5733")
                    put("weight", "bold")
                    put("style", "dashed")
                },
            )

        assertEquals(expected, actual)
    }
}

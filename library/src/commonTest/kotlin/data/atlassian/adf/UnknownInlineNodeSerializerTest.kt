package data.atlassian.adf

import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import kotlin.test.Test
import kotlin.test.assertEquals

class UnknownInlineNodeSerializerTest {
    @Test
    fun deserializeWithTypeOnly() {
        val json = """
        {
            "type": "customInline"
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
                    "content": [$json]
                }
            ]
        }
        """,
            )

        val paragraph = doc.content.first() as ParagraphNode
        val actual = paragraph.content?.first()

        val expected =
            UnknownInlineNode(
                type = "customInline",
                attrs = null,
            )

        assertEquals(expected, actual)
    }

    @Test
    fun deserializeWithAttrs() {
        val json = """
        {
            "type": "fake_mention",
            "attrs": {
                "id": "user-123",
                "displayName": "John Doe",
                "accessLevel": "CONTAINER"
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
                    "content": [$json]
                }
            ]
        }
        """,
            )

        val paragraph = doc.content.first() as ParagraphNode
        val actual = paragraph.content?.first()

        val expected =
            UnknownInlineNode(
                type = "fake_mention",
                attrs =
                buildJsonObject {
                    put("id", "user-123")
                    put("displayName", "John Doe")
                    put("accessLevel", "CONTAINER")
                },
            )

        assertEquals(expected, actual)
    }
}

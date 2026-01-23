package io.ncipollo.transcribe.data.atlassian.adf

import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import kotlin.test.Test
import kotlin.test.assertEquals

class UnknownBlockNodeSerializerTest {
    @Test
    fun deserializeWithTypeOnly() {
        val json = """
        {
            "type": "futureBlock"
        }
        """

        val doc = ADFSerializer.fromJson("""{"type": "doc", "version": 1, "content": [$json]}""")
        val actual = doc.content.first()

        val expected =
            UnknownBlockNode(
                type = "futureBlock",
                attrs = null,
                content = null,
            )

        assertEquals(expected, actual)
    }

    @Test
    fun deserializeWithAttrs() {
        val json = """
        {
            "type": "customBlock",
            "attrs": {
                "customId": "123",
                "enabled": true
            }
        }
        """

        val doc = ADFSerializer.fromJson("""{"type": "doc", "version": 1, "content": [$json]}""")
        val actual = doc.content.first()

        val expected =
            UnknownBlockNode(
                type = "customBlock",
                attrs =
                buildJsonObject {
                    put("customId", "123")
                    put("enabled", true)
                },
                content = null,
            )

        assertEquals(expected, actual)
    }

    @Test
    fun deserializeWithContent() {
        val json = """
        {
            "type": "nestedBlock",
            "content": [
                {
                    "type": "paragraph",
                    "content": [
                        {
                            "type": "text",
                            "text": "Nested content"
                        }
                    ]
                }
            ]
        }
        """

        val doc = ADFSerializer.fromJson("""{"type": "doc", "version": 1, "content": [$json]}""")
        val actual = doc.content.first()

        val expected =
            UnknownBlockNode(
                type = "nestedBlock",
                attrs = null,
                content =
                listOf(
                    ParagraphNode(
                        content =
                        listOf(
                            TextNode(text = "Nested content"),
                        ),
                    ),
                ),
            )

        assertEquals(expected, actual)
    }

    @Test
    fun deserializeWithAllProperties() {
        val json = """
        {
            "type": "fullBlock",
            "attrs": {
                "id": "block-1",
                "metadata": "test"
            },
            "content": [
                {
                    "type": "paragraph",
                    "content": [
                        {
                            "type": "text",
                            "text": "Content here"
                        }
                    ]
                },
                {
                    "type": "heading",
                    "attrs": {
                        "level": 2
                    },
                    "content": [
                        {
                            "type": "text",
                            "text": "Subheading"
                        }
                    ]
                }
            ]
        }
        """

        val doc = ADFSerializer.fromJson("""{"type": "doc", "version": 1, "content": [$json]}""")
        val actual = doc.content.first()

        val expected =
            UnknownBlockNode(
                type = "fullBlock",
                attrs =
                buildJsonObject {
                    put("id", "block-1")
                    put("metadata", "test")
                },
                content =
                listOf(
                    ParagraphNode(
                        content =
                        listOf(
                            TextNode(text = "Content here"),
                        ),
                    ),
                    HeadingNode(
                        attrs = HeadingAttrs(level = 2),
                        content =
                        listOf(
                            TextNode(text = "Subheading"),
                        ),
                    ),
                ),
            )

        assertEquals(expected, actual)
    }
}

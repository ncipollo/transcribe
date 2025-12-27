package fixtures.adf

import data.atlassian.adf.DocNode
import data.atlassian.adf.HeadingAttrs
import data.atlassian.adf.HeadingNode
import data.atlassian.adf.ParagraphNode
import data.atlassian.adf.TextNode

object SimpleADFDocumentFixture {
    const val SIMPLE_DOCUMENT = """
{
    "type": "doc",
    "version": 1,
    "content": [
        {
            "type": "paragraph",
            "content": [
                {
                    "type": "text",
                    "text": "Hello, world!"
                }
            ]
        },
        {
            "type": "heading",
            "attrs": {
                "level": 1
            },
            "content": [
                {
                    "type": "text",
                    "text": "Test Heading"
                }
            ]
        }
    ]
}
"""

    val expectedSimpleDocNode: DocNode
        get() =
            DocNode(
                version = 1,
                content =
                    listOf(
                        ParagraphNode(
                            content =
                                listOf(
                                    TextNode(text = "Hello, world!"),
                                ),
                        ),
                        HeadingNode(
                            attrs = HeadingAttrs(level = 1),
                            content =
                                listOf(
                                    TextNode(text = "Test Heading"),
                                ),
                        ),
                    ),
            )
}

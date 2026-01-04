package transcribe.atlassian

import context.ADFTranscriberContext
import context.PageContext
import data.atlassian.adf.ExtensionAttrs
import data.atlassian.adf.ExtensionNode
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import kotlin.test.Test
import kotlin.test.assertEquals

class DrawioExtensionTranscriberTest {
    private val transcriber = DrawioExtensionTranscriber()
    private val pageContext = PageContext(title = "Test Page")
    private val context = ADFTranscriberContext(
        pageContext = pageContext,
    )

    @Test
    fun transcribe_withTitle() {
        val parameters =
            buildJsonObject {
                put(
                    "macroParams",
                    buildJsonObject {
                        put(
                            "diagramName",
                            buildJsonObject {
                                put("value", "Diagram Name.drawio")
                            },
                        )
                        put(
                            "diagramDisplayName",
                            buildJsonObject {
                                put("value", "draw.io Diagram")
                            },
                        )
                    },
                )
            }
        val node =
            ExtensionNode(
                attrs =
                ExtensionAttrs(
                    extensionKey = "drawio",
                    extensionType = "com.atlassian.confluence.macro.core",
                    parameters = parameters,
                ),
            )

        val result = transcriber.transcribe(node, context)

        assertEquals("![draw.io Diagram](test_page/diagram_name.drawio)\n", result.content)
    }

    @Test
    fun transcribe_withoutTitle() {
        val parameters =
            buildJsonObject {
                put(
                    "macroParams",
                    buildJsonObject {
                        put(
                            "diagramName",
                            buildJsonObject {
                                put("value", "My Diagram.drawio")
                            },
                        )
                    },
                )
            }
        val node =
            ExtensionNode(
                attrs =
                ExtensionAttrs(
                    extensionKey = "drawio",
                    extensionType = "com.atlassian.confluence.macro.core",
                    parameters = parameters,
                ),
            )

        val result = transcriber.transcribe(node, context)

        assertEquals("![](test_page/my_diagram.drawio)\n", result.content)
    }

    @Test
    fun transcribe_withoutParameters() {
        val node =
            ExtensionNode(
                attrs =
                ExtensionAttrs(
                    extensionKey = "drawio",
                    extensionType = "com.atlassian.confluence.macro.core",
                    parameters = null,
                ),
            )

        val result = transcriber.transcribe(node, context)

        assertEquals("", result.content)
    }

    @Test
    fun transcribe_withoutMacroParams() {
        val parameters =
            buildJsonObject {
                put("someOtherKey", "value")
            }
        val node =
            ExtensionNode(
                attrs =
                ExtensionAttrs(
                    extensionKey = "drawio",
                    extensionType = "com.atlassian.confluence.macro.core",
                    parameters = parameters,
                ),
            )

        val result = transcriber.transcribe(node, context)

        assertEquals("", result.content)
    }
}

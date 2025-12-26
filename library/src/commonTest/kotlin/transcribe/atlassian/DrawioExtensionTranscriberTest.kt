package transcribe.atlassian

import data.atlassian.adf.ExtensionAttrs
import data.atlassian.adf.ExtensionNode
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import kotlin.test.Test
import kotlin.test.assertEquals

class DrawioExtensionTranscriberTest {
    private val transcriber = DrawioExtensionTranscriber()
    private val context = ADFTranscriberContext()

    @Test
    fun transcribe_withTitle() {
        val parameters =
            buildJsonObject {
                put(
                    "macroMetadata",
                    buildJsonObject {
                        put("title", "draw.io Diagram")
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

        assertEquals("![draw.io Diagram](images/image.png)\n", result.content)
    }

    @Test
    fun transcribe_withoutTitle() {
        val parameters =
            buildJsonObject {
                put(
                    "macroMetadata",
                    buildJsonObject {},
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

        assertEquals("![](images/image.png)\n", result.content)
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

        assertEquals("![](images/image.png)\n", result.content)
    }

    @Test
    fun transcribe_withoutMacroMetadata() {
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

        assertEquals("![](images/image.png)\n", result.content)
    }
}

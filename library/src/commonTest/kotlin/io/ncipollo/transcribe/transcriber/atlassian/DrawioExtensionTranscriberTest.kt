package io.ncipollo.transcribe.transcriber.atlassian

import io.ncipollo.transcribe.context.ADFTranscriberContext
import io.ncipollo.transcribe.context.PageContext
import io.ncipollo.transcribe.data.atlassian.adf.ExtensionAttrs
import io.ncipollo.transcribe.data.atlassian.adf.ExtensionNode
import kotlinx.serialization.json.buildJsonArray
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import io.ncipollo.transcribe.transcriber.TranscribeResult
import io.ncipollo.transcribe.transcriber.action.AttachmentDownload
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
                put(
                    "macroMetadata",
                    buildJsonObject {
                        put(
                            "placeholder",
                            buildJsonArray {
                                add(
                                    buildJsonObject {
                                        put(
                                            "data",
                                            buildJsonObject {
                                                put("url", "https://example.com/diagram.drawio")
                                            },
                                        )
                                    },
                                )
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
        val expected = TranscribeResult(
            content = "![draw.io Diagram](test_page/diagram_name.drawio.png)\n",
            actions = listOf(
                AttachmentDownload(
                    downloadPath = "https://example.com/diagram.drawio",
                    localRelativePath = "test_page/diagram_name.drawio.png",
                ),
            ),
        )
        val result = transcriber.transcribe(node, context)
        assertEquals(expected, result)
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
                put(
                    "macroMetadata",
                    buildJsonObject {
                        put(
                            "placeholder",
                            buildJsonArray {
                                add(
                                    buildJsonObject {
                                        put(
                                            "data",
                                            buildJsonObject {
                                                put("url", "https://example.com/my_diagram.drawio")
                                            },
                                        )
                                    },
                                )
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
        val expected = TranscribeResult(
            content = "![](test_page/my_diagram.drawio.png)\n",
            actions = listOf(
                AttachmentDownload(
                    downloadPath = "https://example.com/my_diagram.drawio",
                    localRelativePath = "test_page/my_diagram.drawio.png",
                ),
            ),
        )
        val result = transcriber.transcribe(node, context)
        assertEquals(expected, result)
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
        val expected = TranscribeResult("")
        val result = transcriber.transcribe(node, context)
        assertEquals(expected, result)
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
        val expected = TranscribeResult("")
        val result = transcriber.transcribe(node, context)
        assertEquals(expected, result)
    }

    @Test
    fun transcribe_missingDownloadUrl() {
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
                put(
                    "macroMetadata",
                    buildJsonObject {
                        put("placeholder", buildJsonArray { })
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
        val expected = TranscribeResult("")
        val result = transcriber.transcribe(node, context)
        assertEquals(expected, result)
    }
}

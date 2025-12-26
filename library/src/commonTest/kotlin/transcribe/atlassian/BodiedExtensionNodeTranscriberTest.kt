package transcribe.atlassian

import data.atlassian.adf.BodiedExtensionNode
import data.atlassian.adf.ExtensionAttrs
import transcribe.TranscribeResult
import kotlin.test.Test
import kotlin.test.assertEquals

class BodiedExtensionNodeTranscriberTest {
    private val context = ADFTranscriberContext()

    @Test
    fun transcribe_usesCustomMapperWhenAvailable() {
        val customTranscriber = TestBodiedExtensionNodeTranscriber("custom-result")
        val customMapper = ExtensionMapper(mapOf("test-extension:key" to { customTranscriber }))
        val defaultMapper = ExtensionMapper(emptyMap())
        val transcriber = BodiedExtensionNodeTranscriber(customMapper, defaultMapper)

        val node =
            BodiedExtensionNode(attrs = ExtensionAttrs(extensionKey = "key", extensionType = "test-extension"), content = emptyList())
        val result = transcriber.transcribe(node, context)

        assertEquals("custom-result", result.content)
    }

    @Test
    fun transcribe_fallsBackToDefaultMapperWhenCustomNotAvailable() {
        val defaultTranscriber = TestBodiedExtensionNodeTranscriber("default-result")
        val customMapper = ExtensionMapper(emptyMap())
        val defaultMapper = ExtensionMapper(mapOf("test-extension:key" to { defaultTranscriber }))
        val transcriber = BodiedExtensionNodeTranscriber(customMapper, defaultMapper)

        val node =
            BodiedExtensionNode(attrs = ExtensionAttrs(extensionKey = "key", extensionType = "test-extension"), content = emptyList())
        val result = transcriber.transcribe(node, context)

        assertEquals("default-result", result.content)
    }

    @Test
    fun transcribe_customMapperOverridesDefaultMapper() {
        val customTranscriber = TestBodiedExtensionNodeTranscriber("custom-result")
        val defaultTranscriber = TestBodiedExtensionNodeTranscriber("default-result")
        val customMapper = ExtensionMapper(mapOf("test-extension:key" to { customTranscriber }))
        val defaultMapper = ExtensionMapper(mapOf("test-extension:key" to { defaultTranscriber }))
        val transcriber = BodiedExtensionNodeTranscriber(customMapper, defaultMapper)

        val node =
            BodiedExtensionNode(attrs = ExtensionAttrs(extensionKey = "key", extensionType = "test-extension"), content = emptyList())
        val result = transcriber.transcribe(node, context)

        assertEquals("custom-result", result.content)
    }

    @Test
    fun transcribe_returnsEmptyStringWhenExtensionTypeNotFound() {
        val customMapper = ExtensionMapper(emptyMap())
        val defaultMapper = ExtensionMapper(emptyMap())
        val transcriber = BodiedExtensionNodeTranscriber(customMapper, defaultMapper)

        val node =
            BodiedExtensionNode(attrs = ExtensionAttrs(extensionKey = "key", extensionType = "unknown-extension"), content = emptyList())
        val result = transcriber.transcribe(node, context)

        assertEquals("", result.content)
    }

    @Test
    fun transcribe_usesDefaultMapperWhenCustomNotProvided() {
        val defaultTranscriber = TestBodiedExtensionNodeTranscriber("default-result")
        val defaultMapper = ExtensionMapper(mapOf("test-extension:key" to { defaultTranscriber }))
        val transcriber = BodiedExtensionNodeTranscriber(defaultExtensionMapper = defaultMapper)

        val node =
            BodiedExtensionNode(attrs = ExtensionAttrs(extensionKey = "key", extensionType = "test-extension"), content = emptyList())
        val result = transcriber.transcribe(node, context)

        assertEquals("default-result", result.content)
    }

    @Test
    fun transcribe_usesEmptyMapperWhenNeitherProvided() {
        val transcriber = BodiedExtensionNodeTranscriber()

        val node =
            BodiedExtensionNode(attrs = ExtensionAttrs(extensionKey = "key", extensionType = "unknown-extension"), content = emptyList())
        val result = transcriber.transcribe(node, context)

        assertEquals("", result.content)
    }

    private class TestBodiedExtensionNodeTranscriber(private val result: String) : ADFTranscriber<BodiedExtensionNode> {
        override fun transcribe(
            input: BodiedExtensionNode,
            context: ADFTranscriberContext,
        ): TranscribeResult<String> = TranscribeResult(result)
    }
}

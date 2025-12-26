package transcribe.atlassian

import data.atlassian.adf.ExtensionAttrs
import data.atlassian.adf.ExtensionNode
import transcribe.TranscribeResult
import kotlin.test.Test
import kotlin.test.assertEquals

class ExtensionNodeTranscriberTest {
    private val context = ADFTranscriberContext()

    @Test
    fun transcribe_usesCustomMapperWhenAvailable() {
        val customTranscriber = TestExtensionNodeTranscriber("custom-result")
        val customMapper = ExtensionMapper(mapOf("test-extension:key" to { customTranscriber }))
        val defaultMapper = ExtensionMapper(emptyMap())
        val transcriber = ExtensionNodeTranscriber(customMapper, defaultMapper)

        val node = ExtensionNode(attrs = ExtensionAttrs(extensionKey = "key", extensionType = "test-extension"))
        val result = transcriber.transcribe(node, context)

        assertEquals("custom-result", result.content)
    }

    @Test
    fun transcribe_fallsBackToDefaultMapperWhenCustomNotAvailable() {
        val defaultTranscriber = TestExtensionNodeTranscriber("default-result")
        val customMapper = ExtensionMapper(emptyMap())
        val defaultMapper = ExtensionMapper(mapOf("test-extension:key" to { defaultTranscriber }))
        val transcriber = ExtensionNodeTranscriber(customMapper, defaultMapper)

        val node = ExtensionNode(attrs = ExtensionAttrs(extensionKey = "key", extensionType = "test-extension"))
        val result = transcriber.transcribe(node, context)

        assertEquals("default-result", result.content)
    }

    @Test
    fun transcribe_customMapperOverridesDefaultMapper() {
        val customTranscriber = TestExtensionNodeTranscriber("custom-result")
        val defaultTranscriber = TestExtensionNodeTranscriber("default-result")
        val customMapper = ExtensionMapper(mapOf("test-extension:key" to { customTranscriber }))
        val defaultMapper = ExtensionMapper(mapOf("test-extension:key" to { defaultTranscriber }))
        val transcriber = ExtensionNodeTranscriber(customMapper, defaultMapper)

        val node = ExtensionNode(attrs = ExtensionAttrs(extensionKey = "key", extensionType = "test-extension"))
        val result = transcriber.transcribe(node, context)

        assertEquals("custom-result", result.content)
    }

    @Test
    fun transcribe_returnsEmptyStringWhenExtensionTypeNotFound() {
        val customMapper = ExtensionMapper(emptyMap())
        val defaultMapper = ExtensionMapper(emptyMap())
        val transcriber = ExtensionNodeTranscriber(customMapper, defaultMapper)

        val node = ExtensionNode(attrs = ExtensionAttrs(extensionKey = "key", extensionType = "unknown-extension"))
        val result = transcriber.transcribe(node, context)

        assertEquals("", result.content)
    }

    @Test
    fun transcribe_usesDefaultMapperWhenCustomNotProvided() {
        val defaultTranscriber = TestExtensionNodeTranscriber("default-result")
        val defaultMapper = ExtensionMapper(mapOf("test-extension:key" to { defaultTranscriber }))
        val transcriber = ExtensionNodeTranscriber(defaultExtensionMapper = defaultMapper)

        val node = ExtensionNode(attrs = ExtensionAttrs(extensionKey = "key", extensionType = "test-extension"))
        val result = transcriber.transcribe(node, context)

        assertEquals("default-result", result.content)
    }

    @Test
    fun transcribe_usesEmptyMapperWhenNeitherProvided() {
        val transcriber = ExtensionNodeTranscriber()

        val node = ExtensionNode(attrs = ExtensionAttrs(extensionKey = "key", extensionType = "unknown-extension"))
        val result = transcriber.transcribe(node, context)

        assertEquals("", result.content)
    }

    private class TestExtensionNodeTranscriber(private val result: String) : ADFTranscriber<ExtensionNode> {
        override fun transcribe(
            input: ExtensionNode,
            context: ADFTranscriberContext,
        ): TranscribeResult<String> = TranscribeResult(result)
    }
}

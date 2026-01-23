package io.ncipollo.transcribe.transcriber.atlassian

import io.ncipollo.transcribe.context.ADFTranscriberContext
import io.ncipollo.transcribe.data.atlassian.adf.InlineExtensionAttrs
import io.ncipollo.transcribe.data.atlassian.adf.InlineExtensionNode
import io.ncipollo.transcribe.transcriber.TranscribeResult
import kotlin.test.Test
import kotlin.test.assertEquals

class InlineExtensionNodeTranscriberTest {
    private val context = ADFTranscriberContext()

    @Test
    fun transcribe_usesCustomMapperWhenAvailable() {
        val customTranscriber = TestInlineExtensionNodeTranscriber("custom-result")
        val customMapper = ExtensionMapper(mapOf("test-extension:key" to { customTranscriber }))
        val defaultMapper = ExtensionMapper(emptyMap())
        val transcriber = InlineExtensionNodeTranscriber(customMapper, defaultMapper)

        val node = InlineExtensionNode(attrs = InlineExtensionAttrs(extensionKey = "key", extensionType = "test-extension"))
        val result = transcriber.transcribe(node, context)

        assertEquals("custom-result", result.content)
    }

    @Test
    fun transcribe_fallsBackToDefaultMapperWhenCustomNotAvailable() {
        val defaultTranscriber = TestInlineExtensionNodeTranscriber("default-result")
        val customMapper = ExtensionMapper(emptyMap())
        val defaultMapper = ExtensionMapper(mapOf("test-extension:key" to { defaultTranscriber }))
        val transcriber = InlineExtensionNodeTranscriber(customMapper, defaultMapper)

        val node = InlineExtensionNode(attrs = InlineExtensionAttrs(extensionKey = "key", extensionType = "test-extension"))
        val result = transcriber.transcribe(node, context)

        assertEquals("default-result", result.content)
    }

    @Test
    fun transcribe_customMapperOverridesDefaultMapper() {
        val customTranscriber = TestInlineExtensionNodeTranscriber("custom-result")
        val defaultTranscriber = TestInlineExtensionNodeTranscriber("default-result")
        val customMapper = ExtensionMapper(mapOf("test-extension:key" to { customTranscriber }))
        val defaultMapper = ExtensionMapper(mapOf("test-extension:key" to { defaultTranscriber }))
        val transcriber = InlineExtensionNodeTranscriber(customMapper, defaultMapper)

        val node = InlineExtensionNode(attrs = InlineExtensionAttrs(extensionKey = "key", extensionType = "test-extension"))
        val result = transcriber.transcribe(node, context)

        assertEquals("custom-result", result.content)
    }

    @Test
    fun transcribe_returnsEmptyStringWhenExtensionTypeNotFound() {
        val customMapper = ExtensionMapper(emptyMap())
        val defaultMapper = ExtensionMapper(emptyMap())
        val transcriber = InlineExtensionNodeTranscriber(customMapper, defaultMapper)

        val node = InlineExtensionNode(attrs = InlineExtensionAttrs(extensionKey = "key", extensionType = "unknown-extension"))
        val result = transcriber.transcribe(node, context)

        assertEquals("", result.content)
    }

    @Test
    fun transcribe_usesDefaultMapperWhenCustomNotProvided() {
        val defaultTranscriber = TestInlineExtensionNodeTranscriber("default-result")
        val defaultMapper = ExtensionMapper(mapOf("test-extension:key" to { defaultTranscriber }))
        val transcriber = InlineExtensionNodeTranscriber(defaultExtensionMapper = defaultMapper)

        val node = InlineExtensionNode(attrs = InlineExtensionAttrs(extensionKey = "key", extensionType = "test-extension"))
        val result = transcriber.transcribe(node, context)

        assertEquals("default-result", result.content)
    }

    @Test
    fun transcribe_usesEmptyMapperWhenNeitherProvided() {
        val transcriber = InlineExtensionNodeTranscriber()

        val node = InlineExtensionNode(attrs = InlineExtensionAttrs(extensionKey = "key", extensionType = "unknown-extension"))
        val result = transcriber.transcribe(node, context)

        assertEquals("", result.content)
    }

    private class TestInlineExtensionNodeTranscriber(private val result: String) : ADFTranscriber<InlineExtensionNode> {
        override fun transcribe(
            input: InlineExtensionNode,
            context: ADFTranscriberContext,
        ): TranscribeResult<String> = TranscribeResult(result)
    }
}

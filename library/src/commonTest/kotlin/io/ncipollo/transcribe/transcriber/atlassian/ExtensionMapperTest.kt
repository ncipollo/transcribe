package io.ncipollo.transcribe.transcriber.atlassian

import io.ncipollo.transcribe.context.ADFTranscriberContext
import io.ncipollo.transcribe.data.atlassian.adf.TextNode
import io.ncipollo.transcribe.transcriber.TranscribeResult
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class ExtensionMapperTest {
    @Test
    fun transcriberFor_returnsTranscriberForRegisteredExtensionType() {
        val testTranscriber = TestExtensionTranscriber()
        val mapper =
            ExtensionMapper(
                mapOf("test-extension" to { testTranscriber }),
            )

        val result = mapper.transcriberFor("test-extension")

        assertNotNull(result)
        assertEquals(testTranscriber, result)
    }

    @Test
    fun transcriberFor_returnsNullForUnregisteredExtensionType() {
        val mapper = ExtensionMapper(emptyMap())

        val result = mapper.transcriberFor("unknown-extension")

        assertNull(result)
    }

    @Test
    fun plus_mergesMappers() {
        val transcriber1 = TestExtensionTranscriber()
        val transcriber2 = TestExtensionTranscriber()
        val mapper1 = ExtensionMapper(mapOf("ext1" to { transcriber1 }))
        val mapper2 = ExtensionMapper(mapOf("ext2" to { transcriber2 }))

        val merged = mapper1 + mapper2

        assertNotNull(merged.transcriberFor("ext1"))
        assertNotNull(merged.transcriberFor("ext2"))
    }

    @Test
    fun plus_laterMapperOverridesEarlier() {
        val transcriber1 = TestExtensionTranscriber()
        val transcriber2 = TestExtensionTranscriber()
        val mapper1 = ExtensionMapper(mapOf("ext1" to { transcriber1 }))
        val mapper2 = ExtensionMapper(mapOf("ext1" to { transcriber2 }))

        val merged = mapper1 + mapper2

        val result = merged.transcriberFor("ext1")
        assertEquals(transcriber2, result)
    }

    @Test
    fun transcriberFor_passesMapperToFactory() {
        var receivedMapper: ExtensionMapper? = null
        val mapper =
            ExtensionMapper(
                mapOf(
                    "test-extension" to { m ->
                        receivedMapper = m
                        TestExtensionTranscriber()
                    },
                ),
            )

        mapper.transcriberFor("test-extension")

        assertEquals(mapper, receivedMapper)
    }

    private class TestExtensionTranscriber : ADFTranscriber<TextNode> {
        override fun transcribe(
            input: TextNode,
            context: ADFTranscriberContext,
        ): TranscribeResult<String> = TranscribeResult("")
    }
}

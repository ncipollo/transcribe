package io.ncipollo.transcribe.transcriber.atlassian

import io.ncipollo.transcribe.context.ADFTranscriberContext
import io.ncipollo.transcribe.data.atlassian.adf.TextNode
import io.ncipollo.transcribe.transcriber.TranscribeResult
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class ExtensionMapperBuilderTest {
    @Test
    fun add_registersExtensionType() {
        val transcriber = TestExtensionTranscriber()
        val builder = ExtensionMapperBuilder()

        builder.add("test-extension") { transcriber }
        val mapper = builder.build()

        assertNotNull(mapper.transcriberFor("test-extension"))
    }

    @Test
    fun add_returnsBuilderForChaining() {
        val builder = ExtensionMapperBuilder()

        val result = builder.add("ext1") { TestExtensionTranscriber() }

        assertEquals(builder, result)
    }

    @Test
    fun add_canRegisterMultipleExtensionTypes() {
        val builder = ExtensionMapperBuilder()
        val transcriber1 = TestExtensionTranscriber()
        val transcriber2 = TestExtensionTranscriber()

        builder
            .add("ext1") { transcriber1 }
            .add("ext2") { transcriber2 }
        val mapper = builder.build()

        assertNotNull(mapper.transcriberFor("ext1"))
        assertNotNull(mapper.transcriberFor("ext2"))
    }

    @Test
    fun mapper_buildsSameMapperAsBuild() {
        val builder = ExtensionMapperBuilder()
        builder.add("test-extension") { TestExtensionTranscriber() }

        val mapper1 = builder.mapper()
        val mapper2 = builder.build()

        // Both should have the same transcriber registered
        assertNotNull(mapper1.transcriberFor("test-extension"))
        assertNotNull(mapper2.transcriberFor("test-extension"))
    }

    @Test
    fun extensionMapper_dslFunction() {
        val mapper =
            extensionMapper {
                add("ext1") { TestExtensionTranscriber() }
                add("ext2") { TestExtensionTranscriber() }
            }

        assertNotNull(mapper.transcriberFor("ext1"))
        assertNotNull(mapper.transcriberFor("ext2"))
    }

    private class TestExtensionTranscriber : ADFTranscriber<TextNode> {
        override fun transcribe(
            input: TextNode,
            context: ADFTranscriberContext,
        ): TranscribeResult<String> = TranscribeResult("")
    }
}

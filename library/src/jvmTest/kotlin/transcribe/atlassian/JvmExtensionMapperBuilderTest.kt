package transcribe.atlassian

import context.ADFTranscriberContext
import data.atlassian.adf.TextNode
import transcribe.TranscribeResult
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class JvmExtensionMapperBuilderTest {
    @Test
    fun add_registersExtensionType() {
        val transcriber = TestExtensionTranscriber()
        val builder = JvmExtensionMapperBuilder()

        builder.add("test-extension") { transcriber }
        val mapper = builder.build()

        assertNotNull(mapper.transcriberFor("test-extension"))
    }

    @Test
    fun add_returnsBuilderForChaining() {
        val builder = JvmExtensionMapperBuilder()

        val result = builder.add("ext1") { TestExtensionTranscriber() }

        assertEquals(builder, result)
    }

    @Test
    fun add_canRegisterMultipleExtensionTypes() {
        val builder = JvmExtensionMapperBuilder()
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
    fun build_implementsInterface() {
        val builder = JvmExtensionMapperBuilder()
        builder.add("test-extension") { TestExtensionTranscriber() }

        val buildable: ExtensionTranscriberMapBuildable = builder
        val mapper = buildable.build()

        assertNotNull(mapper.transcriberFor("test-extension"))
    }

    private class TestExtensionTranscriber : ADFTranscriber<TextNode> {
        override fun transcribe(
            input: TextNode,
            context: ADFTranscriberContext,
        ): TranscribeResult<String> = TranscribeResult("")
    }
}

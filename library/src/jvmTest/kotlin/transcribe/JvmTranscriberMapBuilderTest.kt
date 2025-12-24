package transcribe

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class JvmTranscriberMapBuilderTest {

    @Test
    fun add_javaClass() {
        val transcriber1 = TestTranscriber("transcriber1")
        val transcriber2 = TestTranscriber("transcriber2")

        val map = JvmTranscriberMapBuilder<TestNode, TestTranscriber>()
            .add(TestNodeA::class.java, transcriber1)
            .add(TestNodeB::class.java, transcriber2)
            .build()

        assertEquals(2, map.size)
        assertEquals(transcriber1, map[TestNodeA::class])
        assertEquals(transcriber2, map[TestNodeB::class])
    }

    @Test
    fun add_multipleJavaClasses() {
        val transcriber1 = TestTranscriber("transcriber1")
        val transcriber2 = TestTranscriber("transcriber2")
        val transcriber3 = TestTranscriber("transcriber3")

        val map = JvmTranscriberMapBuilder<TestNode, TestTranscriber>()
            .add(TestNodeA::class.java, transcriber1)
            .add(TestNodeB::class.java, transcriber2)
            .add(TestNodeC::class.java, transcriber3)
            .build()

        assertEquals(3, map.size)
        assertEquals(transcriber1, map[TestNodeA::class])
        assertEquals(transcriber2, map[TestNodeB::class])
        assertEquals(transcriber3, map[TestNodeC::class])
    }

    @Test
    fun add_builderStyleChaining() {
        val transcriber1 = TestTranscriber("transcriber1")
        val transcriber2 = TestTranscriber("transcriber2")

        val map = JvmTranscriberMapBuilder<TestNode, TestTranscriber>()
            .add(TestNodeA::class.java, transcriber1)
            .add(TestNodeB::class.java, transcriber2)
            .build()

        assertEquals(2, map.size)
        assertEquals(transcriber1, map[TestNodeA::class])
        assertEquals(transcriber2, map[TestNodeB::class])
    }

    @Test
    fun build_returnsImmutableCopy() {
        val builder = JvmTranscriberMapBuilder<TestNode, TestTranscriber>()
        val transcriber = TestTranscriber("transcriber")

        builder.add(TestNodeA::class.java, transcriber)
        val map1 = builder.build()

        builder.add(TestNodeB::class.java, transcriber)
        val map2 = builder.build()

        // First map should only have 1 entry, second should have 2
        assertEquals(1, map1.size)
        assertEquals(2, map2.size)
        assertTrue(map1.containsKey(TestNodeA::class))
        assertTrue(map2.containsKey(TestNodeA::class))
        assertTrue(map2.containsKey(TestNodeB::class))
    }

    // Test helper classes
    sealed interface TestNode

    data class TestNodeA(val value: String = "A") : TestNode
    data class TestNodeB(val value: String = "B") : TestNode
    data class TestNodeC(val value: String = "C") : TestNode

    class TestTranscriber(private val name: String) : Transcriber<TestNode, String> {
        override fun transcribe(input: TestNode): TranscribeResult<String> {
            return TranscribeResult("$name: $input")
        }
    }
}

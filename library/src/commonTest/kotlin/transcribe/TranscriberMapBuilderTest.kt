package transcribe

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class TranscriberMapBuilderTest {

    @Test
    fun add_reifiedGeneric() {
        val transcriber1 = TestTranscriber("transcriber1")
        val transcriber2 = TestTranscriber("transcriber2")

        val map = TranscriberMapBuilder<TestNode, TestTranscriber>()
            .add<TestNodeA>(transcriber1)
            .add<TestNodeB>(transcriber2)
            .toMap()

        assertEquals(2, map.size)
        assertEquals(transcriber1, map[TestNodeA::class])
        assertEquals(transcriber2, map[TestNodeB::class])
    }

    @Test
    fun add_kclass() {
        val transcriber1 = TestTranscriber("transcriber1")
        val transcriber2 = TestTranscriber("transcriber2")

        val map = TranscriberMapBuilder<TestNode, TestTranscriber>()
            .add(TestNodeA::class, transcriber1)
            .add(TestNodeB::class, transcriber2)
            .toMap()

        assertEquals(2, map.size)
        assertEquals(transcriber1, map[TestNodeA::class])
        assertEquals(transcriber2, map[TestNodeB::class])
    }

    @Test
    fun addAll_existingMap() {
        val transcriber1 = TestTranscriber("transcriber1")
        val transcriber2 = TestTranscriber("transcriber2")
        val transcriber3 = TestTranscriber("transcriber3")

        val existingMap = mapOf(
            TestNodeA::class to transcriber1,
            TestNodeB::class to transcriber2
        )

        val map = TranscriberMapBuilder<TestNode, TestTranscriber>()
            .addAll(existingMap)
            .add<TestNodeC>(transcriber3)
            .toMap()

        assertEquals(3, map.size)
        assertEquals(transcriber1, map[TestNodeA::class])
        assertEquals(transcriber2, map[TestNodeB::class])
        assertEquals(transcriber3, map[TestNodeC::class])
    }

    @Test
    fun transcriberMap_dslStyle() {
        val transcriber1 = TestTranscriber("transcriber1")
        val transcriber2 = TestTranscriber("transcriber2")

        val map = transcriberMap<TestNode, TestTranscriber> {
            add<TestNodeA>(transcriber1)
            add<TestNodeB>(transcriber2)
        }

        assertEquals(2, map.size)
        assertEquals(transcriber1, map[TestNodeA::class])
        assertEquals(transcriber2, map[TestNodeB::class])
    }

    @Test
    fun toMap_returnsImmutableCopy() {
        val builder = TranscriberMapBuilder<TestNode, TestTranscriber>()
        val transcriber = TestTranscriber("transcriber")

        builder.add<TestNodeA>(transcriber)
        val map1 = builder.toMap()

        builder.add<TestNodeB>(transcriber)
        val map2 = builder.toMap()

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


package transcribe.atlassian

import kotlin.test.Test
import kotlin.test.assertEquals

class DefaultADFNodeMapTest {

    @Test
    fun defaultADFNodeMap_containsExpectedCount() {
        val map = defaultADFNodeMap()
        assertEquals(19, map.size)
    }
}


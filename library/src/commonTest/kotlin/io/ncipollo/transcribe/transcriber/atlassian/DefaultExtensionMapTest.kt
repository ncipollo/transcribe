package io.ncipollo.transcribe.transcriber.atlassian

import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class DefaultExtensionMapTest {
    @Test
    fun defaultExtensionMapper_createsMapper() {
        val mapper = defaultExtensionMapper()
        assertNotNull(mapper)
    }

    @Test
    fun defaultExtensionMapper_initiallyEmpty() {
        val mapper = defaultExtensionMapper()
        val transcriber = mapper.transcriberFor("any-extension-type")
        assertNull(transcriber)
    }
}

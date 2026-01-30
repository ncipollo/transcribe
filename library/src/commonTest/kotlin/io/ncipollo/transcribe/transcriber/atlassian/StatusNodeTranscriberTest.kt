package io.ncipollo.transcribe.transcriber.atlassian

import io.ncipollo.transcribe.context.ADFTranscriberContext
import io.ncipollo.transcribe.data.atlassian.adf.StatusAttrs
import io.ncipollo.transcribe.data.atlassian.adf.StatusColor
import io.ncipollo.transcribe.data.atlassian.adf.StatusNode
import kotlin.test.Test
import kotlin.test.assertEquals

class StatusNodeTranscriberTest {
    private val transcriber = StatusNodeTranscriber()
    private val context = ADFTranscriberContext()

    @Test
    fun transcribe_blue() {
        val node = StatusNode(attrs = StatusAttrs(text = "In Progress", color = StatusColor.BLUE))
        val result = transcriber.transcribe(node, context)
        assertEquals("🔵 IN PROGRESS", result.content)
    }

    @Test
    fun transcribe_green() {
        val node = StatusNode(attrs = StatusAttrs(text = "Done", color = StatusColor.GREEN))
        val result = transcriber.transcribe(node, context)
        assertEquals("🟢 DONE", result.content)
    }

    @Test
    fun transcribe_red() {
        val node = StatusNode(attrs = StatusAttrs(text = "Blocked", color = StatusColor.RED))
        val result = transcriber.transcribe(node, context)
        assertEquals("🔴 BLOCKED", result.content)
    }

    @Test
    fun transcribe_yellow() {
        val node = StatusNode(attrs = StatusAttrs(text = "In Review", color = StatusColor.YELLOW))
        val result = transcriber.transcribe(node, context)
        assertEquals("🟡 IN REVIEW", result.content)
    }

    @Test
    fun transcribe_purple() {
        val node = StatusNode(attrs = StatusAttrs(text = "On Hold", color = StatusColor.PURPLE))
        val result = transcriber.transcribe(node, context)
        assertEquals("🟣 ON HOLD", result.content)
    }

    @Test
    fun transcribe_neutral() {
        val node = StatusNode(attrs = StatusAttrs(text = "Pending", color = StatusColor.NEUTRAL))
        val result = transcriber.transcribe(node, context)
        assertEquals("⚪ PENDING", result.content)
    }

    @Test
    fun transcribe_lowercaseText() {
        val node = StatusNode(attrs = StatusAttrs(text = "in progress", color = StatusColor.BLUE))
        val result = transcriber.transcribe(node, context)
        assertEquals("🔵 IN PROGRESS", result.content)
    }
}

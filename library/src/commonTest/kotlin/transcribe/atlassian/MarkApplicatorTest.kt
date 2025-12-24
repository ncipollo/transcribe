package transcribe.atlassian

import data.atlassian.adf.BackgroundColorAttrs
import data.atlassian.adf.BackgroundColorMark
import data.atlassian.adf.CodeMark
import data.atlassian.adf.EmMark
import data.atlassian.adf.LinkAttrs
import data.atlassian.adf.LinkMark
import data.atlassian.adf.StrikeMark
import data.atlassian.adf.StrongMark
import data.atlassian.adf.SubSupAttrs
import data.atlassian.adf.SubSupMark
import data.atlassian.adf.SubSupType
import data.atlassian.adf.TextColorAttrs
import data.atlassian.adf.TextColorMark
import data.atlassian.adf.UnderlineMark
import kotlin.test.Test
import kotlin.test.assertEquals

class MarkApplicatorTest {

    @Test
    fun applyMarks_strongMark() {
        val result = MarkApplicator.applyMarks("hello", listOf(StrongMark))
        assertEquals("**hello**", result)
    }

    @Test
    fun applyMarks_emMark() {
        val result = MarkApplicator.applyMarks("hello", listOf(EmMark))
        assertEquals("*hello*", result)
    }

    @Test
    fun applyMarks_codeMark() {
        val result = MarkApplicator.applyMarks("hello", listOf(CodeMark))
        assertEquals("`hello`", result)
    }

    @Test
    fun applyMarks_strikeMark() {
        val result = MarkApplicator.applyMarks("hello", listOf(StrikeMark))
        assertEquals("~~hello~~", result)
    }

    @Test
    fun applyMarks_underlineMark() {
        val result = MarkApplicator.applyMarks("hello", listOf(UnderlineMark))
        assertEquals("<u>hello</u>", result)
    }

    @Test
    fun applyMarks_linkMark() {
        val result = MarkApplicator.applyMarks("hello", listOf(LinkMark(LinkAttrs(href = "https://example.com"))))
        assertEquals("[hello](https://example.com)", result)
    }

    @Test
    fun applyMarks_textColorMark() {
        val result = MarkApplicator.applyMarks("hello", listOf(TextColorMark(TextColorAttrs(color = "#FF0000"))))
        assertEquals("<span style=\"color: #FF0000\">hello</span>", result)
    }

    @Test
    fun applyMarks_backgroundColorMark() {
        val result = MarkApplicator.applyMarks("hello", listOf(BackgroundColorMark(BackgroundColorAttrs(color = "#FFFF00"))))
        assertEquals("<span style=\"background-color: #FFFF00\">hello</span>", result)
    }

    @Test
    fun applyMarks_subSupMark_subscript() {
        val result = MarkApplicator.applyMarks("hello", listOf(SubSupMark(SubSupAttrs(type = SubSupType.SUB))))
        assertEquals("<sub>hello</sub>", result)
    }

    @Test
    fun applyMarks_subSupMark_superscript() {
        val result = MarkApplicator.applyMarks("hello", listOf(SubSupMark(SubSupAttrs(type = SubSupType.SUP))))
        assertEquals("<sup>hello</sup>", result)
    }

    @Test
    fun applyMarks_nullMarks() {
        val result = MarkApplicator.applyMarks("hello", null)
        assertEquals("hello", result)
    }

    @Test
    fun applyMarks_emptyMarks() {
        val result = MarkApplicator.applyMarks("hello", emptyList())
        assertEquals("hello", result)
    }

    @Test
    fun applyMarks_multipleMarks() {
        val result = MarkApplicator.applyMarks(
            "hello",
            listOf(
                StrongMark,
                EmMark,
                LinkMark(LinkAttrs(href = "https://example.com"))
            )
        )
        assertEquals("[***hello***](https://example.com)", result)
    }
}


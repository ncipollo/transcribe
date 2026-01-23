package io.ncipollo.transcribe.transcriber.atlassian

import io.ncipollo.transcribe.data.atlassian.adf.BackgroundColorAttrs
import io.ncipollo.transcribe.data.atlassian.adf.BackgroundColorMark
import io.ncipollo.transcribe.data.atlassian.adf.CodeMark
import io.ncipollo.transcribe.data.atlassian.adf.EmMark
import io.ncipollo.transcribe.data.atlassian.adf.LinkAttrs
import io.ncipollo.transcribe.data.atlassian.adf.LinkMark
import io.ncipollo.transcribe.data.atlassian.adf.StrikeMark
import io.ncipollo.transcribe.data.atlassian.adf.StrongMark
import io.ncipollo.transcribe.data.atlassian.adf.SubSupAttrs
import io.ncipollo.transcribe.data.atlassian.adf.SubSupMark
import io.ncipollo.transcribe.data.atlassian.adf.SubSupType
import io.ncipollo.transcribe.data.atlassian.adf.TextColorAttrs
import io.ncipollo.transcribe.data.atlassian.adf.TextColorMark
import io.ncipollo.transcribe.data.atlassian.adf.UnderlineMark
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
        val result =
            MarkApplicator.applyMarks(
                "hello",
                listOf(
                    StrongMark,
                    EmMark,
                    LinkMark(LinkAttrs(href = "https://example.com")),
                ),
            )
        assertEquals("[***hello***](https://example.com)", result)
    }

    @Test
    fun applyMarks_strongMark_withLeadingSpace() {
        val result = MarkApplicator.applyMarks(" hello", listOf(StrongMark))
        assertEquals(" **hello**", result)
    }

    @Test
    fun applyMarks_strongMark_withTrailingSpace() {
        val result = MarkApplicator.applyMarks("hello ", listOf(StrongMark))
        assertEquals("**hello** ", result)
    }

    @Test
    fun applyMarks_strongMark_withLeadingAndTrailingSpaces() {
        val result = MarkApplicator.applyMarks(" hello ", listOf(StrongMark))
        assertEquals(" **hello** ", result)
    }

    @Test
    fun applyMarks_codeMark_withLeadingSpace() {
        val result = MarkApplicator.applyMarks(" hello", listOf(CodeMark))
        assertEquals(" `hello`", result)
    }

    @Test
    fun applyMarks_codeMark_withTrailingSpace() {
        val result = MarkApplicator.applyMarks("hello ", listOf(CodeMark))
        assertEquals("`hello` ", result)
    }

    @Test
    fun applyMarks_codeMark_withLeadingAndTrailingSpaces() {
        val result = MarkApplicator.applyMarks(" hello ", listOf(CodeMark))
        assertEquals(" `hello` ", result)
    }

    @Test
    fun applyMarks_multipleMarks_withSpaces() {
        val result =
            MarkApplicator.applyMarks(
                " hello ",
                listOf(
                    StrongMark,
                    EmMark,
                ),
            )
        assertEquals(" ***hello*** ", result)
    }
}

package transcribe.markdown

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class DetailsHtmlParserTest {
    @Test
    fun parse_validDetails_returnsParsedDetails() {
        val html = "<details><summary>Collapse</summary>- Content which is kinda hidden.</details>"
        val result = DetailsHtmlParser.parse(html)

        assertEquals("Collapse", result?.summary)
        assertEquals("- Content which is kinda hidden.", result?.content)
    }

    @Test
    fun parse_detailsWithWhitespace_returnsTrimmedContent() {
        val html = """
            <details>
            <summary>  Summary Text  </summary>
            Content here
            </details>
        """.trimIndent()
        val result = DetailsHtmlParser.parse(html)

        assertEquals("Summary Text", result?.summary)
        assertEquals("Content here", result?.content)
    }

    @Test
    fun parse_detailsWithNestedMarkdown_returnsContent() {
        val html = """
            <details>
            <summary>Click to expand</summary>
            # Heading
            - List item 1
            - List item 2
            </details>
        """.trimIndent()
        val result = DetailsHtmlParser.parse(html)

        assertEquals("Click to expand", result?.summary)
        assertEquals("# Heading\n- List item 1\n- List item 2", result?.content)
    }

    @Test
    fun parse_detailsCaseInsensitive_returnsParsedDetails() {
        val html = "<DETAILS><SUMMARY>Test</SUMMARY>Content</DETAILS>"
        val result = DetailsHtmlParser.parse(html)

        assertEquals("Test", result?.summary)
        assertEquals("Content", result?.content)
    }

    @Test
    fun parse_detailsWithAttributes_returnsParsedDetails() {
        val html = "<details open><summary class='test'>Summary</summary>Content</details>"
        val result = DetailsHtmlParser.parse(html)

        assertEquals("Summary", result?.summary)
        assertEquals("Content", result?.content)
    }

    @Test
    fun parse_emptyDetails_returnsEmptyStrings() {
        val html = "<details><summary></summary></details>"
        val result = DetailsHtmlParser.parse(html)

        assertEquals("", result?.summary)
        assertEquals("", result?.content)
    }

    @Test
    fun parse_missingDetailsTag_returnsNull() {
        val html = "<div><summary>Test</summary>Content</div>"
        val result = DetailsHtmlParser.parse(html)

        assertNull(result)
    }

    @Test
    fun parse_missingSummaryTag_returnsNull() {
        val html = "<details>Content</details>"
        val result = DetailsHtmlParser.parse(html)

        assertNull(result)
    }

    @Test
    fun parse_missingClosingDetailsTag_returnsNull() {
        val html = "<details><summary>Test</summary>Content"
        val result = DetailsHtmlParser.parse(html)

        assertNull(result)
    }

    @Test
    fun parse_missingClosingSummaryTag_returnsNull() {
        val html = "<details><summary>TestContent</details>"
        val result = DetailsHtmlParser.parse(html)

        assertNull(result)
    }
}


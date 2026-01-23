package io.ncipollo.transcribe.api.atlassian

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class ConfluenceUrlParserTest {
    @Test
    fun extractPageId_spacesFormat_returnsPageId() {
        val url = "https://transcribe.atlassian.net/wiki/spaces/SPACE/pages/123456/Page+Title"
        val pageId = ConfluenceUrlParser.extractPageId(url)
        assertEquals("123456", pageId)
    }

    @Test
    fun extractPageId_spacesFormat_withDifferentSpace_returnsPageId() {
        val url = "https://transcribe.atlassian.net/wiki/spaces/ENG/pages/789012/Some+Page"
        val pageId = ConfluenceUrlParser.extractPageId(url)
        assertEquals("789012", pageId)
    }

    @Test
    fun extractPageId_viewpageActionFormat_returnsPageId() {
        val url = "https://transcribe.atlassian.net/wiki/pages/viewpage.action?pageId=123456"
        val pageId = ConfluenceUrlParser.extractPageId(url)
        assertEquals("123456", pageId)
    }

    @Test
    fun extractPageId_viewpageActionFormat_withAdditionalParams_returnsPageId() {
        val url = "https://transcribe.atlassian.net/wiki/pages/viewpage.action?pageId=123456&spaceKey=SPACE"
        val pageId = ConfluenceUrlParser.extractPageId(url)
        assertEquals("123456", pageId)
    }

    @Test
    fun extractPageId_invalidUrl_returnsNull() {
        val url = "https://example.com/some/path"
        val pageId = ConfluenceUrlParser.extractPageId(url)
        assertNull(pageId)
    }

    @Test
    fun extractPageId_spacesFormat_missingPageId_returnsNull() {
        val url = "https://transcribe.atlassian.net/wiki/spaces/SPACE/pages/"
        val pageId = ConfluenceUrlParser.extractPageId(url)
        assertNull(pageId)
    }

    @Test
    fun extractPageId_viewpageActionFormat_missingPageId_returnsNull() {
        val url = "https://transcribe.atlassian.net/wiki/pages/viewpage.action"
        val pageId = ConfluenceUrlParser.extractPageId(url)
        assertNull(pageId)
    }

    @Test
    fun extractPageId_viewpageActionFormat_emptyPageId_returnsNull() {
        val url = "https://transcribe.atlassian.net/wiki/pages/viewpage.action?pageId="
        val pageId = ConfluenceUrlParser.extractPageId(url)
        assertNull(pageId)
    }

    @Test
    fun extractPageId_viewpageActionFormat_nonNumericPageId_returnsNull() {
        val url = "https://transcribe.atlassian.net/wiki/pages/viewpage.action?pageId=abc123"
        val pageId = ConfluenceUrlParser.extractPageId(url)
        assertNull(pageId)
    }

    @Test
    fun extractPageId_emptyString_returnsNull() {
        val url = ""
        val pageId = ConfluenceUrlParser.extractPageId(url)
        assertNull(pageId)
    }

    @Test
    fun extractPageId_spacesFormat_withQueryParams_returnsPageId() {
        val url = "https://transcribe.atlassian.net/wiki/spaces/SPACE/pages/123456/Page+Title?param=value"
        val pageId = ConfluenceUrlParser.extractPageId(url)
        assertEquals("123456", pageId)
    }
}

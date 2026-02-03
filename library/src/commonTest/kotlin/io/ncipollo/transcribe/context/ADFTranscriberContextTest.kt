package io.ncipollo.transcribe.context

import kotlin.test.Test
import kotlin.test.assertEquals

class ADFTranscriberContextTest {
    @Test
    fun suggestedDocumentName_titleCase() {
        val context = ADFTranscriberContext(
            pageContext = PageContext(title = "Title Case"),
        )

        val result = context.suggestedDocumentName

        assertEquals("title_case", result)
    }

    @Test
    fun suggestedDocumentName_emptyTitle() {
        val context = ADFTranscriberContext(
            pageContext = PageContext(title = ""),
        )

        val result = context.suggestedDocumentName

        assertEquals("", result)
    }

    @Test
    fun suggestedDocumentName_withExtension() {
        val context = ADFTranscriberContext(
            pageContext = PageContext(title = "This.txt"),
        )

        val result = context.suggestedDocumentName

        assertEquals("this", result)
    }

    @Test
    fun suggestedImageFolder_emptyTitle() {
        val context = ADFTranscriberContext(
            pageContext = PageContext(title = ""),
        )

        val result = context.suggestedImageFolder

        assertEquals("images", result)
    }

    @Test
    fun suggestedImageFolder_titleWithNoExtension() {
        val context = ADFTranscriberContext(
            pageContext = PageContext(title = "Test Title"),
        )

        val result = context.suggestedImageFolder

        assertEquals("test_title", result)
    }

    @Test
    fun suggestedImageFolder_titleWithExtensionAndMultipleSpaces() {
        val context = ADFTranscriberContext(
            pageContext = PageContext(title = "Test Title.PNG"),
        )

        val result = context.suggestedImageFolder

        assertEquals("test_title", result)
    }

    @Test
    fun suggestedImageFolder_blankTitle() {
        val context = ADFTranscriberContext(
            pageContext = PageContext(title = "   "),
        )

        val result = context.suggestedImageFolder

        assertEquals("images", result)
    }

    @Test
    fun pageUrl_defaultIsEmpty() {
        val context = ADFTranscriberContext()

        val result = context.pageUrl

        assertEquals("", result)
    }

    @Test
    fun pageUrl_returnsProvidedValue() {
        val context = ADFTranscriberContext(
            pageUrl = "https://test.atlassian.net/wiki/spaces/TEST/pages/123",
        )

        val result = context.pageUrl

        assertEquals("https://test.atlassian.net/wiki/spaces/TEST/pages/123", result)
    }
}

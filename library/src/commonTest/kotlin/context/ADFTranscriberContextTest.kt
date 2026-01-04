package context

import kotlin.test.Test
import kotlin.test.assertEquals

class ADFTranscriberContextTest {
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
}

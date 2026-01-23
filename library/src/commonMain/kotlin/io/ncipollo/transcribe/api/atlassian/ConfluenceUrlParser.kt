package io.ncipollo.transcribe.api.atlassian

/**
 * Utility class for parsing Confluence URLs to extract page IDs.
 * Supports multiple Confluence URL formats.
 */
object ConfluenceUrlParser {
    /**
     * Extract page ID from various Confluence URL formats.
     *
     * Supports:
     * - https://transcribe.atlassian.net/wiki/spaces/SPACE/pages/123456/Page+Title
     * - https://transcribe.atlassian.net/wiki/pages/viewpage.action?pageId=123456
     *
     * @param url Full Confluence page URL
     * @return Page ID as string, or null if not found
     */
    fun extractPageId(url: String): String? {
        // Pattern 1: /spaces/SPACE/pages/123456/...
        val spacesPattern = Regex("""/spaces/[^/]+/pages/(\d+)""")
        val spacesMatch = spacesPattern.find(url)
        if (spacesMatch != null) {
            return spacesMatch.groupValues[1]
        }

        // Pattern 2: /pages/viewpage.action?pageId=123456
        if (url.contains("/pages/viewpage.action")) {
            val queryStartIndex = url.indexOf('?')
            if (queryStartIndex != -1) {
                val queryString = url.substring(queryStartIndex + 1)
                val params = queryString.split('&')
                for (param in params) {
                    val keyValue = param.split('=', limit = 2)
                    if (keyValue.size == 2 && keyValue[0] == "pageId") {
                        val pageId = keyValue[1]
                        // Remove any URL-encoded characters and verify it's numeric
                        if (pageId.matches(Regex("""\d+"""))) {
                            return pageId
                        }
                    }
                }
            }
        }

        return null
    }
}

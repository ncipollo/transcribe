import api.atlassian.ConfluenceHttpClientFactory
import api.atlassian.ConfluenceUrlParser
import api.atlassian.PageAPIClient
import io.ktor.client.HttpClient
import transcribe.atlassian.ADFTranscriberContext
import transcribe.atlassian.ConfluenceToMarkdownTranscriber

/**
 * Main entry point for transcribing Confluence pages to Markdown.
 * Provides methods to fetch and transcribe Confluence pages.
 */
class Transcribe(
    private val configuration: TranscribeConfiguration = TranscribeConfiguration(),
) {
    private val httpClient: HttpClient by lazy {
        ConfluenceHttpClientFactory.create(
            siteName = configuration.confluenceConfiguration.siteName,
            authMaterial = configuration.confluenceConfiguration.authMaterial,
        )
    }

    private val pageApiClient: PageAPIClient by lazy {
        PageAPIClient(httpClient)
    }

    private val transcriber: ConfluenceToMarkdownTranscriber by lazy {
        ConfluenceToMarkdownTranscriber(
            customTranscribers = configuration.adfCustomTranscribers,
        )
    }

    /**
     * Fetches a Confluence page by URL and returns its content as Markdown.
     *
     * @param url The full Confluence page URL
     * @return The page content as Markdown string
     * @throws IllegalArgumentException if the URL cannot be parsed to extract a page ID
     * @throws Exception if the page cannot be fetched or transcribed
     */
    suspend fun getPageMarkdown(url: String): String {
        val pageId =
            ConfluenceUrlParser.extractPageId(url)
                ?: throw IllegalArgumentException("Unable to extract page ID from URL: $url")

        val page = pageApiClient.getPage(pageId)
        val adfBody =
            page.body?.atlasDocFormat?.docNode
                ?: throw IllegalStateException("Page $pageId does not contain ADF body content")

        val result = transcriber.transcribe(adfBody, ADFTranscriberContext())
        return result.content
    }

    /**
     * Closes the HTTP client and releases resources.
     * Should be called when the Transcribe instance is no longer needed.
     */
    fun close() {
        httpClient.close()
    }
}

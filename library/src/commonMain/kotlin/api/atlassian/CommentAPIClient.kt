package api.atlassian

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

class CommentAPIClient(private val httpClient: HttpClient) {
    /**
     * Fetches all footer comments for a given page ID.
     * Handles pagination automatically by following the `_links.next` URL until all pages are retrieved.
     *
     * @param pageId The page ID to fetch comments for
     * @return A list of all footer comments for the page
     */
    suspend fun getPageFooterComments(pageId: String): List<FooterComment> {
        val allComments = mutableListOf<FooterComment>()
        var nextUrl: String? = "pages/$pageId/footer-comments?body-format=atlas_doc_format"

        while (nextUrl != null) {
            val response = httpClient.get(nextUrl).body<FooterCommentsResponse>()
            allComments.addAll(response.results)
            nextUrl = response.links?.next
        }

        return allComments
    }

    /**
     * Fetches all inline comments for a given page ID.
     * Handles pagination automatically by following the `_links.next` URL until all pages are retrieved.
     *
     * @param pageId The page ID to fetch comments for
     * @return A list of all inline comments for the page
     */
    suspend fun getPageInlineComments(pageId: String): List<InlineComment> {
        val allComments = mutableListOf<InlineComment>()
        var nextUrl: String? = "pages/$pageId/inline-comments?body-format=atlas_doc_format"

        while (nextUrl != null) {
            val response = httpClient.get(nextUrl).body<InlineCommentsResponse>()
            allComments.addAll(response.results)
            nextUrl = response.links?.next
        }

        return allComments
    }
}

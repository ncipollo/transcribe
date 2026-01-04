package api.atlassian

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

class AttachmentAPIClient(private val httpClient: HttpClient) {
    /**
     * Fetches all attachments for a given page ID.
     * Handles pagination automatically by following the `_links.next` URL until all pages are retrieved.
     *
     * @param pageId The page ID to fetch attachments for
     * @return A list of all attachments for the page
     */
    suspend fun getPageAttachments(pageId: String): List<Attachment> {
        val allAttachments = mutableListOf<Attachment>()
        var nextUrl: String? = "pages/$pageId/attachments"

        while (nextUrl != null) {
            val response = httpClient.get(nextUrl).body<AttachmentsResponse>()
            allAttachments.addAll(response.results)
            nextUrl = response.links?.next
        }

        return allAttachments
    }

    /**
     * Fetches the binary data for an attachment/image asset from the given URL path.
     *
     * @param urlPath The URL path to fetch the attachment data from
     * @return The binary data as a ByteArray
     */
    suspend fun getAttachmentData(urlPath: String): ByteArray {
        return httpClient.get(urlPath).body<ByteArray>()
    }
}

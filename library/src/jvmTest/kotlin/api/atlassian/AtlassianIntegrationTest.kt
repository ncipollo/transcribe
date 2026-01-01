package api.atlassian

import kotlinx.coroutines.runBlocking
import transcribe.atlassian.ADFTranscriberContext
import transcribe.atlassian.ConfluenceToMarkdownTranscriber
import kotlin.test.Test
import kotlin.test.assertNotNull

class AtlassianIntegrationTest {
    @Test
    fun getPage_fetchesPageFromConfluence() =
        runBlocking {
            val apiToken = System.getenv("ATLASSIAN_API_TOKEN")
            val email = System.getenv("ATLASSIAN_EMAIL")
            val siteName = System.getenv("ATLASSIAN_SITE_NAME")
            val pageId = System.getenv("ATLASSIAN_TEST_PAGE_ID")

            // Skip test if credentials are not available
            if (apiToken == null || email == null || siteName == null || pageId == null) {
                return@runBlocking
            }

            val authMaterial = AtlassianAuthMaterial(email = email, apiToken = apiToken)
            val httpClient = ConfluenceHttpClientFactory.create(siteName, authMaterial)
            val pageClient = PageAPIClient(httpClient)

            val page = pageClient.getPage(pageId)

            assertNotNull(page)
            assertNotNull(page.id)
            assertNotNull(page.title)
            assertNotNull(page.spaceId)

            // Verify ADF body can be parsed
            val adfBody = page.body?.atlasDocFormat?.docNode
            assertNotNull(adfBody)
            assertNotNull(adfBody.content)

            // Transcribe ADF to Markdown
            val transcriber = ConfluenceToMarkdownTranscriber()
            val result = transcriber.transcribe(adfBody, ADFTranscriberContext())
            println("-----------")
            println(result.content)

            httpClient.close()
        }
}

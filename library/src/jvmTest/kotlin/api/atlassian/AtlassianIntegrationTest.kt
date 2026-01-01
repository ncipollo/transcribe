package api.atlassian

import Transcribe
import TranscribeConfiguration
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class AtlassianIntegrationTest {
    @Test
    fun getPageMarkdown_fetchesAndTranscribesPage() =
        runBlocking {
            val apiToken = System.getenv("ATLASSIAN_API_TOKEN")
            val email = System.getenv("ATLASSIAN_EMAIL")
            val siteName = System.getenv("ATLASSIAN_SITE_NAME")
            val pageUrl = System.getenv("ATLASSIAN_TEST_PAGE_URL")

            // Skip test if credentials are not available
            if (apiToken == null || email == null || siteName == null || pageUrl == null) {
                return@runBlocking
            }

            val configuration =
                TranscribeConfiguration(
                    confluenceConfiguration =
                        ConfluenceConfiguration(
                            siteName = siteName,
                            authMaterial = AtlassianAuthMaterial(email = email, apiToken = apiToken),
                        ),
                )
            val transcribe = Transcribe(configuration)

            val markdown = transcribe.getPageMarkdown(pageUrl)

            assertNotNull(markdown)
            assertTrue(markdown.isNotEmpty())

            println("-----------")
            println(markdown)

            transcribe.close()
        }
}

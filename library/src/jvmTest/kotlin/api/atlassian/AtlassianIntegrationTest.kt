package api.atlassian

import Transcribe
import TranscribeConfiguration
import fixtures.markdown.ComplexMarkdownFixture
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

    @Test
    fun updatePageMarkdown_updatesPageWithMarkdown() =
        runBlocking {
            val apiToken = System.getenv("ATLASSIAN_API_TOKEN")
            val email = System.getenv("ATLASSIAN_EMAIL")
            val siteName = System.getenv("ATLASSIAN_SITE_NAME")
            val updatePageUrl = System.getenv("ATLASSIAN_UPDATE_TEST_PAGE_URL")

            // Skip test if credentials are not available
            if (apiToken == null || email == null || siteName == null || updatePageUrl == null) {
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

            val markdown = ComplexMarkdownFixture.COMPLEX_MARKDOWN

            val updatedPage =
                transcribe.updatePageMarkdown(
                    url = updatePageUrl,
                    markdown = markdown,
                    message = "Integration test update",
                )

            assertNotNull(updatedPage)
            assertNotNull(updatedPage.id)
            assertNotNull(updatedPage.title)
            assertTrue(updatedPage.title.isNotEmpty())

            println("-----------")
            println("Updated page ID: ${updatedPage.id}")
            println("Updated page title: ${updatedPage.title}")
            println("Page version: ${updatedPage.version.number}")

            transcribe.close()
        }
}

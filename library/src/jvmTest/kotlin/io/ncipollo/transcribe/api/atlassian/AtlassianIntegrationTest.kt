package io.ncipollo.transcribe.api.atlassian

import io.ncipollo.transcribe.Transcribe
import io.ncipollo.transcribe.TranscribeConfiguration
import io.ncipollo.transcribe.fixtures.markdown.ComplexMarkdownFixture
import kotlinx.coroutines.runBlocking
import java.io.File
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

            val result = transcribe.getPageMarkdown(pageUrl)

            assertNotNull(result)
            assertNotNull(result.markdown)
            assertTrue(result.markdown.isNotEmpty())

            println("-----------")
            println(result.markdown)

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

    @Test
    fun updateTemplateMarkdown_updatesTemplateWithMarkdown() =
        runBlocking {
            val apiToken = System.getenv("ATLASSIAN_API_TOKEN")
            val email = System.getenv("ATLASSIAN_EMAIL")
            val siteName = System.getenv("ATLASSIAN_SITE_NAME")
            val templateId = System.getenv("ATLASSIAN_TEMPLATE_ID")

            // Skip test if credentials are not available
            if (apiToken == null || email == null || siteName == null || templateId == null) {
                return@runBlocking
            }

            val markdown = ComplexMarkdownFixture.COMPLEX_MARKDOWN

            updateTemplateAndVerify(
                apiToken = apiToken,
                email = email,
                siteName = siteName,
                templateId = templateId,
                markdown = markdown,
            )
        }

    @Test
    fun updateTemplateMarkdown_updatesTemplateWithMarkdownFromFile() =
        runBlocking {
            val apiToken = System.getenv("ATLASSIAN_API_TOKEN")
            val email = System.getenv("ATLASSIAN_EMAIL")
            val siteName = System.getenv("ATLASSIAN_SITE_NAME")
            val templateId = System.getenv("ATLASSIAN_TEMPLATE_ID")
            val markdownFilePath = System.getenv("ATLASSIAN_MARKDOWN_FILE_PATH")

            // Skip test if credentials or file path are not available
            if (apiToken == null || email == null || siteName == null || templateId == null || markdownFilePath == null) {
                return@runBlocking
            }

            val markdownFile = File(markdownFilePath)
            if (!markdownFile.exists()) {
                println("Markdown file not found at: $markdownFilePath")
                return@runBlocking
            }

            val markdown = markdownFile.readText()

            updateTemplateAndVerify(
                apiToken = apiToken,
                email = email,
                siteName = siteName,
                templateId = templateId,
                markdown = markdown,
            )
        }

    private suspend fun updateTemplateAndVerify(
        apiToken: String,
        email: String,
        siteName: String,
        templateId: String,
        markdown: String,
    ) {
        val configuration =
            TranscribeConfiguration(
                confluenceConfiguration =
                ConfluenceConfiguration(
                    siteName = siteName,
                    authMaterial = AtlassianAuthMaterial(email = email, apiToken = apiToken),
                ),
            )
        val transcribe = Transcribe(configuration)

        val updatedTemplate =
            transcribe.updateTemplateMarkdown(
                templateId = templateId,
                markdown = markdown,
                name = "Test Template",
            )

        assertNotNull(updatedTemplate)
        assertNotNull(updatedTemplate.templateId)
        assertNotNull(updatedTemplate.name)
        assertTrue(updatedTemplate.name.isNotEmpty())

        println("-----------")
        println("Updated template ID: ${updatedTemplate.templateId}")
        println("Updated template name: ${updatedTemplate.name}")
        println("Template type: ${updatedTemplate.templateType}")

        transcribe.close()
    }
}

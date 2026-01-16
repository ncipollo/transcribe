package feature

import api.atlassian.TemplateAPIClient
import api.atlassian.TemplateResponse
import api.atlassian.TemplateSpace
import context.MarkdownContext
import data.atlassian.adf.DocNode
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.runBlocking
import transcribe.TranscribeResult
import transcribe.markdown.MarkdownToConfluenceTranscriber
import transcribe.transformer.ADFTransformer
import kotlin.test.Test
import kotlin.test.assertEquals

class TemplateMarkdownUpdateFeatureTest {
    private val templateApiClient = mockk<TemplateAPIClient>()
    private val markdownTranscriber = mockk<MarkdownToConfluenceTranscriber>()
    private val toConfluenceTransformer = mockk<ADFTransformer<MarkdownContext>>()

    private val feature = TemplateMarkdownUpdateFeature(
        templateApiClient = templateApiClient,
        markdownTranscriber = markdownTranscriber,
        toConfluenceTransformer = toConfluenceTransformer,
    )

    @Test
    fun update_transcribesMarkdownAndUpdatesTemplate() = runBlocking {
        val templateId = "template-123"
        val markdown = "# Test"
        val name = "Test Template"
        val templateType = "page"

        val currentTemplate = TemplateResponse(
            templateId = templateId,
            name = name,
            description = "Test description",
            labels = emptyList(),
            templateType = templateType,
            body = null,
            space = TemplateSpace(key = "TEST"),
        )

        val docNode = DocNode(content = emptyList())
        val transcribeResult = TranscribeResult(content = docNode)

        val updatedTemplate = currentTemplate.copy(name = "Updated")

        coEvery { templateApiClient.getTemplate(templateId) } returns currentTemplate
        coEvery { markdownTranscriber.transcribe(markdown, any()) } returns transcribeResult
        coEvery { toConfluenceTransformer.transform(any(), any()) } returns emptyList()
        coEvery {
            templateApiClient.updateTemplate(
                templateId = templateId,
                name = name,
                docNode = any(),
                templateType = templateType,
                description = currentTemplate.description,
                labels = currentTemplate.labels,
                space = currentTemplate.space,
            )
        } returns updatedTemplate

        val result = feature.update(templateId, markdown, name, templateType)

        assertEquals(updatedTemplate, result)
        coVerify { templateApiClient.getTemplate(templateId) }
        coVerify { markdownTranscriber.transcribe(markdown, any()) }
        coVerify { toConfluenceTransformer.transform(any(), any()) }
    }
}

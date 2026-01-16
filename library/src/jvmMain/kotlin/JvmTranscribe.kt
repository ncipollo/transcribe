import api.atlassian.PageResponse
import api.atlassian.TemplateResponse
import kotlinx.coroutines.runBlocking

/**
 * JVM-friendly wrapper for Transcribe that provides blocking versions of suspend functions.
 * Use this class when calling from Java or when blocking behavior is acceptable.
 */
class JvmTranscribe(
    configuration: TranscribeConfiguration = TranscribeConfiguration(),
) {
    private val transcribe = Transcribe(configuration)

    /**
     * Fetches a Confluence page by URL and returns its content as Markdown.
     * Blocks the current thread until the operation completes.
     */
    fun getPageMarkdown(url: String): PageMarkdownResult = runBlocking {
        transcribe.getPageMarkdown(url)
    }

    /**
     * Updates a Confluence page with markdown content.
     * Blocks the current thread until the operation completes.
     */
    fun updatePageMarkdown(
        url: String,
        markdown: String,
        message: String? = null,
    ): PageResponse = runBlocking {
        transcribe.updatePageMarkdown(url, markdown, message)
    }

    /**
     * Updates a Confluence template with markdown content.
     * Blocks the current thread until the operation completes.
     */
    fun updateTemplateMarkdown(
        templateId: String,
        markdown: String,
        name: String,
        templateType: String = "page",
    ): TemplateResponse = runBlocking {
        transcribe.updateTemplateMarkdown(templateId, markdown, name, templateType)
    }

    /**
     * Closes the underlying HTTP clients and releases resources.
     */
    fun close() {
        transcribe.close()
    }
}

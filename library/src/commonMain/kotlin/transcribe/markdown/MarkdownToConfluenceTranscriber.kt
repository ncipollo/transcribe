package transcribe.markdown

import data.atlassian.adf.DocNode
import data.markdown.parser.MarkdownDocument
import transcribe.TranscribeResult
import transcribe.Transcriber

/**
 * Top-level transcriber for converting Markdown to Confluence ADF documents.
 * Combines default node mappings with custom transcriber overrides.
 */
class MarkdownToConfluenceTranscriber(
    customTranscribers: MarkdownTranscriberMapBuildable = EmptyMarkdownTranscriberMapBuilder(),
) : Transcriber<String, DocNode, MarkdownContext> {
    private val mapper = defaultMarkdownNodeMapper() + customTranscribers.build()
    private val documentTranscriber = MarkdownDocumentTranscriber(mapper)

    override fun transcribe(
        input: String,
        context: MarkdownContext,
    ): TranscribeResult<DocNode> {
        val document = MarkdownDocument.create(input)
        val markdownContext = context.copy(markdownText = input)
        return documentTranscriber.transcribe(document.rootNode, markdownContext)
    }
}

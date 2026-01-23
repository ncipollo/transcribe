package io.ncipollo.transcribe.transcriber.markdown

import io.ncipollo.transcribe.context.MarkdownContext
import io.ncipollo.transcribe.data.atlassian.adf.DocNode
import io.ncipollo.transcribe.data.markdown.parser.MarkdownDocument
import io.ncipollo.transcribe.transcriber.TranscribeResult
import io.ncipollo.transcribe.transcriber.Transcriber

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

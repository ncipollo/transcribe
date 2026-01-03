package transcribe.atlassian

import context.ADFTranscriberContext
import data.atlassian.adf.DocNode
import transcribe.TranscribeResult
import transcribe.Transcriber

/**
 * Top-level transcriber for converting Confluence ADF documents to Markdown.
 * Combines default node mappings with custom transcriber overrides.
 */
class ConfluenceToMarkdownTranscriber(
    customTranscribers: ADFTranscriberMapBuildable = EmptyADFTranscriberMapBuilder(),
) : Transcriber<DocNode, String, ADFTranscriberContext> {
    private val mapper = defaultADFNodeMapper() + customTranscribers.build()
    private val documentTranscriber = ADFDocumentTranscriber(mapper)

    override fun transcribe(
        input: DocNode,
        context: ADFTranscriberContext,
    ): TranscribeResult<String> {
        return documentTranscriber.transcribe(input, context)
    }
}

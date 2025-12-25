package transcribe.atlassian

import data.atlassian.adf.DocNode
import transcribe.TranscribeResult
import transcribe.Transcriber

/**
 * Top-level transcriber for converting Confluence ADF documents to Markdown.
 * Combines default node mappings with custom transcriber overrides.
 */
class ConfluenceToMarkdownTranscriber(
    customTranscribers: ADFTranscriberMapBuildable = EmptyADFTranscriberMapBuilder(),
) : Transcriber<DocNode, String> {
    private val mapper = defaultADFNodeMapper() + customTranscribers.build()
    private val documentTranscriber = ADFDocumentTranscriber(mapper)

    override fun transcribe(input: DocNode): TranscribeResult<String> {
        return documentTranscriber.transcribe(input)
    }
}

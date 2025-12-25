package transcribe.atlassian

import data.atlassian.adf.ADFNode
import data.atlassian.adf.DocNode
import transcribe.EmptyTranscriberMapBuilder
import transcribe.Transcriber
import transcribe.TranscriberMapBuildable
import transcribe.TranscribeResult

/**
 * Top-level transcriber for converting Confluence ADF documents to Markdown.
 * Combines default node mappings with custom transcriber overrides.
 */
class ConfluenceToMarkdownTranscriber(
    customTranscribers: TranscriberMapBuildable<ADFNode, ADFTranscriber<*>> = EmptyTranscriberMapBuilder()
) : Transcriber<DocNode, String> {
    private val nodeMap = defaultADFNodeMap() + customTranscribers.build()
    private val documentTranscriber = ADFDocumentTranscriber(nodeMap)

    override fun transcribe(input: DocNode): TranscribeResult<String> {
        return documentTranscriber.transcribe(input)
    }
}


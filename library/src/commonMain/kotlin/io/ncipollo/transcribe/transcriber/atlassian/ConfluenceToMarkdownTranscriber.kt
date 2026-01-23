package io.ncipollo.transcribe.transcriber.atlassian

import io.ncipollo.transcribe.context.ADFTranscriberContext
import io.ncipollo.transcribe.data.atlassian.adf.DocNode
import io.ncipollo.transcribe.transcriber.TranscribeResult
import io.ncipollo.transcribe.transcriber.Transcriber

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

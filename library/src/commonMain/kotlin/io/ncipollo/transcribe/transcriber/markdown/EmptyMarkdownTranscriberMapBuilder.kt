package io.ncipollo.transcribe.transcriber.markdown

/**
 * A simple implementation of MarkdownTranscriberMapBuildable that returns an empty mapper.
 * Useful when no custom transcriber overrides are needed.
 */
class EmptyMarkdownTranscriberMapBuilder : MarkdownTranscriberMapBuildable {
    override fun build(): MarkdownNodeMapper = markdownNodeMapper { }
}

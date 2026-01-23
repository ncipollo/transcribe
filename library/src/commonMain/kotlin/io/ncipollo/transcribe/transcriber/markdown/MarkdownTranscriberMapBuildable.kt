package io.ncipollo.transcribe.transcriber.markdown

/**
 * Interface for builders that can create markdown node mappers.
 */
interface MarkdownTranscriberMapBuildable {
    /**
     * Build and return a markdown node mapper.
     */
    fun build(): MarkdownNodeMapper
}

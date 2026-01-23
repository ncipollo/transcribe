package io.ncipollo.transcribe.transcriber.atlassian

/**
 * Interface for builders that can create ADF node mappers.
 */
interface ADFTranscriberMapBuildable {
    /**
     * Build and return an ADF node mapper.
     */
    fun build(): ADFNodeMapper
}

package io.ncipollo.transcribe.transcriber.atlassian

/**
 * Interface for builders that can create extension mappers.
 */
interface ExtensionTranscriberMapBuildable {
    /**
     * Build and return an extension mapper.
     */
    fun build(): ExtensionMapper
}

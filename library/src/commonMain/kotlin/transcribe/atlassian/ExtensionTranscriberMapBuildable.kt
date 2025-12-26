package transcribe.atlassian

/**
 * Interface for builders that can create extension mappers.
 */
interface ExtensionTranscriberMapBuildable {
    /**
     * Build and return an extension mapper.
     */
    fun build(): ExtensionMapper
}

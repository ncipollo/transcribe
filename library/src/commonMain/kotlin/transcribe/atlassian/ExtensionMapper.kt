package transcribe.atlassian

/**
 * Mapper that provides transcribers for extension nodes using factory functions.
 * Maps extensionType strings to transcriber factories.
 * This allows transcribers to be created with the final merged mapper,
 * ensuring custom overrides propagate correctly to container transcribers.
 */
class ExtensionMapper(
    private val transcriberFactories: Map<String, (ExtensionMapper) -> ADFTranscriber<*>>,
) {
    fun transcriberFor(extensionType: String): ADFTranscriber<*>? {
        val factory = transcriberFactories[extensionType]
        return factory?.invoke(this)
    }

    operator fun plus(mapper: ExtensionMapper): ExtensionMapper = ExtensionMapper(transcriberFactories + mapper.transcriberFactories)
}

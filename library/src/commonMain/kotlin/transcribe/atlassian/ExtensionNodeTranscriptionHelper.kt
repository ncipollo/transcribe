package transcribe.atlassian

import transcribe.TranscribeResult

/**
 * Helper class that provides common logic for transcribing extension nodes.
 * Merges custom and default extension mappers, with custom taking precedence.
 */
internal class ExtensionNodeTranscriptionHelper(
    customExtensionMapper: ExtensionMapper,
    defaultExtensionMapper: ExtensionMapper,
) {
    private val mergedMapper = defaultExtensionMapper + customExtensionMapper

    /**
     * Transcribes an extension node by looking up the appropriate transcriber
     * for the given extensionType and extensionKey combination.
     *
     * @param extensionType The type of extension to transcribe
     * @param extensionKey The key of the extension to transcribe
     * @param transcribe Function that invokes the transcriber with the appropriate node
     * @return TranscribeResult with the transcribed content, or empty string if no transcriber found
     */
    fun transcribeExtension(
        extensionType: String,
        extensionKey: String,
        transcribe: (ADFTranscriber<*>) -> TranscribeResult<String>,
    ): TranscribeResult<String> {
        val combinedKey = "$extensionType:$extensionKey"
        val transcriber = mergedMapper.transcriberFor(combinedKey) ?: return TranscribeResult("")
        return transcribe(transcriber)
    }
}

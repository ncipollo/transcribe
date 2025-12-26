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
     * for the given extensionType and invoking it.
     *
     * @param extensionType The type of extension to transcribe
     * @param context The transcription context
     * @param transcribe Function that invokes the transcriber with the appropriate node
     * @return TranscribeResult with the transcribed content, or empty string if no transcriber found
     */
    fun transcribeExtension(
        extensionType: String,
        context: ADFTranscriberContext,
        transcribe: (ADFTranscriber<*>) -> TranscribeResult<String>,
    ): TranscribeResult<String> {
        val transcriber = mergedMapper.transcriberFor(extensionType) ?: return TranscribeResult("")
        return transcribe(transcriber)
    }
}

package transcribe.atlassian

import data.atlassian.adf.MediaNode
import data.atlassian.adf.MediaSingleNode
import transcribe.TranscribeResult

/**
 * Transcriber for MediaSingleNode that converts ADF media to markdown image link.
 * Outputs ![alt](images/image.png) format with default path.
 */
class MediaSingleNodeTranscriber : ADFTranscriber<MediaSingleNode> {
    override fun transcribe(input: MediaSingleNode, context: ADFTranscriberContext): TranscribeResult<String> {
        val mediaNode = input.content.firstOrNull() as? MediaNode
        val altText = mediaNode?.attrs?.alt ?: ""
        return TranscribeResult("![$altText](images/image.png)\n")
    }
}


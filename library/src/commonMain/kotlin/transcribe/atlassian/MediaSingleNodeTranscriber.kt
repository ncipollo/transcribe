package transcribe.atlassian

import api.atlassian.Attachment
import context.ADFTranscriberContext
import data.atlassian.adf.MediaNode
import data.atlassian.adf.MediaSingleNode
import files.toSnakeCase
import transcribe.TranscribeResult

/**
 * Transcriber for MediaSingleNode that converts ADF media to markdown image link.
 * Outputs ![alt](path) format using attachment from context.
 */
class MediaSingleNodeTranscriber : ADFTranscriber<MediaSingleNode> {
    override fun transcribe(
        input: MediaSingleNode,
        context: ADFTranscriberContext,
    ): TranscribeResult<String> {
        val mediaNode = input.content.firstOrNull() as? MediaNode ?: return TranscribeResult("")

        val fileId = mediaNode.attrs.id ?: return TranscribeResult("")

        val attachment = context.attachmentContext.attachmentsByFileId[fileId]
            ?: return TranscribeResult("")

        val altText = mediaNode.attrs.alt ?: ""
        val imagePath = imagePath(context, attachment)

        return TranscribeResult("![$altText]($imagePath)\n")
    }

    private fun imagePath(context: ADFTranscriberContext, attachment: Attachment): String {
        val folder = context.suggestedImageFolder
        return "$folder/${attachment.id}_${attachment.title.toSnakeCase()}"
    }
}

package io.ncipollo.transcribe.transcriber.action

import io.ncipollo.transcribe.api.atlassian.AttachmentAPIClient

class ActionHandler(
    private val attachmentApiClient: AttachmentAPIClient,
) {
    /**
     * Handles a list of transcriber actions by filtering for AttachmentDownload actions
     * and downloading their data from the API.
     *
     * @param actions The list of transcriber actions to process
     * @return A list of AttachmentResult objects containing the downloaded data and local paths
     */
    suspend fun handleActions(actions: List<TranscriberAction>): List<AttachmentResult> {
        return actions
            .filterIsInstance<AttachmentDownload>()
            .map { action ->
                AttachmentResult(
                    data = attachmentApiClient.getAttachmentData(action.downloadPath),
                    localRelativePath = action.localRelativePath,
                )
            }
    }
}

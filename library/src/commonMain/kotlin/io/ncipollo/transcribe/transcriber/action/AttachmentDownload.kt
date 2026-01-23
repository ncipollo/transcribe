package io.ncipollo.transcribe.transcriber.action

data class AttachmentDownload(
    val downloadPath: String = "",
    val localRelativePath: String = "",
) : TranscriberAction

package transcribe.action

data class AttachmentDownload(
    val downloadPath: String = "",
    val localRelativePath: String = "",
) : TranscriberAction

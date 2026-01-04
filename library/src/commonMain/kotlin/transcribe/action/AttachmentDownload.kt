package transcribe.action

data class AttachmentDownload(
    val downloadPath: String = "",
) : TranscriberAction

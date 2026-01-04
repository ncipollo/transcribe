package transcribe

import transcribe.action.TranscriberAction

data class TranscribeResult<T>(
    val content: T,
    val actions: List<TranscriberAction> = emptyList(),
)

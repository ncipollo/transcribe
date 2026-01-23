package io.ncipollo.transcribe.transcriber

import io.ncipollo.transcribe.transcriber.action.TranscriberAction

data class TranscribeResult<T>(
    val content: T,
    val actions: List<TranscriberAction> = emptyList(),
)

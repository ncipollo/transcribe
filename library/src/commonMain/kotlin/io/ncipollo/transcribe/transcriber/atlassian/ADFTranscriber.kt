package io.ncipollo.transcribe.transcriber.atlassian

import io.ncipollo.transcribe.context.ADFTranscriberContext
import io.ncipollo.transcribe.data.atlassian.adf.ADFNode
import io.ncipollo.transcribe.transcriber.Transcriber

interface ADFTranscriber<T : ADFNode> : Transcriber<T, String, ADFTranscriberContext>

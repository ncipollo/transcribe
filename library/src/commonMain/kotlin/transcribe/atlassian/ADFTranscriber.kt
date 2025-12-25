package transcribe.atlassian

import data.atlassian.adf.ADFNode
import transcribe.Transcriber

interface ADFTranscriber<T : ADFNode> : Transcriber<T, String, ADFTranscriberContext>
